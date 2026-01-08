<template>
  <div class="login-container">
    <h2 class="login-title">系统登录</h2>

    <!-- 登录错误提示 -->
    <div class="error-message" v-if="errorMessage">
      {{ errorMessage }}
    </div>

    <!-- 验证码错误提示 -->
    <div class="captcha-error-message" v-if="captchaErrorMessage">
      {{ captchaErrorMessage }}
    </div>

    <!-- 登录表单 -->
    <form @submit.prevent="handleLogin">
      <!-- 用户名输入框 -->
      <div class="form-group">
        <label for="username">用户名</label>
        <input type="text" id="username" v-model="username" placeholder="请输入用户名" required>
      </div>

      <!-- 密码输入框 -->
      <div class="form-group">
        <label for="password">密码</label>
        <input type="password" id="password" v-model="password" placeholder="请输入密码" required>
      </div>

      <!-- 验证码输入框与图片 -->
      <div class="form-group">
        <label for="captcha">验证码</label>
        <div class="captcha-group">
          <input type="text" id="captcha" v-model="captcha" class="captcha-input" placeholder="请输入验证码" required>
          <img :src="captchaUrl" alt="验证码" class="captcha-img" @click="refreshCaptcha" title="点击刷新验证码">
        </div>
      </div>

      <!-- 登录按钮 -->
      <button type="submit" class="login-btn">登录</button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'; // 1. 新增导入onMounted
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const username = ref('');
const password = ref('');
const captcha = ref('');
const captchaUrl = ref(''); // 2. 初始值改为空，不再直接拼接时间戳
const errorMessage = ref('');
const captchaErrorMessage = ref('');
const captchaId = ref(''); // 3. 新增：存储X-Captcha-Id的变量

// 刷新验证码（重构）
const refreshCaptcha = async () => {
  try {
    // 4. 通过axios请求验证码接口，获取响应头和图片数据
    const response = await axios.get('/api/captcha', {
      responseType: 'blob', // 关键：指定响应类型为二进制流（图片）
      headers: {
        'Cache-Control': 'no-cache'
      }
    });

    // 5. 获取响应头中的X-Captcha-Id并保存
    captchaId.value = response.headers['x-captcha-id'] || '';

    // 6. 将二进制图片数据转为base64 URL，赋值给captchaUrl
    const blob = new Blob([response.data], { type: 'image/png' });
    captchaUrl.value = URL.createObjectURL(blob);
  } catch (error) {
    console.error('刷新验证码失败：', error);
    errorMessage.value = '验证码加载失败，请刷新页面重试';
  }
};

// 登录处理
const handleLogin = async () => {
  // 校验验证码ID是否存在
  if (!captchaId.value) {
    captchaErrorMessage.value = '请先刷新验证码';
    return;
  }

  try {
    const formData = new FormData();
    formData.append('username', username.value);
    formData.append('password', password.value);
    formData.append('captcha', captcha.value);
    formData.append('captchaId', captchaId.value); // 7. 新增：提交验证码ID给后端

    const response = await axios.post(
        '/api/login',
        formData,
        { withCredentials: true }
    );

    // 登录成功处理
    console.log("=================返回日志====================");
    const data = response.data; // 后端返回的R<T>格式数据
    console.log("成功响应完整数据：", data);
    console.log("成功响应业务数据(token)：", data.data);

    // 保存JWT token
    if (data.data) {
      localStorage.setItem('jwtToken', data.data); //浏览器本地存储
      console.log("token已保存：", localStorage.getItem('jwtToken'));
    }

    await router.push('/');

  } catch (error: any) {
    // 1. 强化错误信息打印（方便调试）
    console.error("=================错误日志====================");
    console.error("请求错误完整信息：", error);
    // 重置错误提示
    errorMessage.value = '';
    captchaErrorMessage.value = '';

    // 2. 判断是否是Axios请求错误（后端有响应）
    if (axios.isAxiosError(error) && error.response) {
      const resData = error.response.data; // 后端返回的R<T>格式错误数据
      console.error("后端返回的错误完整数据：", resData); // 打印后端返回的完整错误数据
      // 读取后端的message字段，设置默认值
      const errorMsg = resData.message || '登录失败，请稍后重试！';
      console.error("提取的错误提示：", errorMsg); // 打印提取的错误信息

      // 3. 根据错误信息内容，区分验证码错误/用户名密码错误
      if (errorMsg.includes('验证码')) {
        captchaErrorMessage.value = errorMsg;
        refreshCaptcha(); // 自动刷新验证码
      } else if (errorMsg.includes('用户名') || errorMsg.includes('密码')) {
        errorMessage.value = errorMsg;
      } else {
        // 其他401/500错误（如未授权、服务器异常）
        errorMessage.value = errorMsg;
      }

      // 3. 网络错误（无后端响应）
    } else {
      console.error("网络错误/非Axios错误：", error);
      errorMessage.value = '登录请求失败，请检查网络连接！';
    }
  }
};

// 8. 新增：页面挂载时初始化验证码
onMounted(() => {
  refreshCaptcha();
});
</script>

<style scoped>
.login-container {
  width: 320px;
  margin: 80px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
  font-size: 24px;
}

.error-message,
.captcha-error-message {
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 4px;
  color: #fff;
  text-align: center;
}

.error-message {
  background-color: #ff4d4f;
}

.captcha-error-message {
  background-color: #faad14;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #555;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

.form-group input:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.captcha-group {
  display: flex;
  gap: 10px;
}

.captcha-input {
  flex: 1;
}

.captcha-img {
  width: 120px;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
}

.login-btn {
  width: 100%;
  padding: 12px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-btn:hover {
  background-color: #40a9ff;
}

.login-btn:active {
  background-color: #096dd9;
}
</style>