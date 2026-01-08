<template>
  <div class="user-list-container">
    <h2 class="page-title">用户列表</h2>
    <div class="user-table-wrapper">
      <table class="user-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>用户名</th>
          <th>真实姓名</th>
          <th>年龄</th>
          <th>性别</th>
          <th>账号状态</th>
          <th>权限代码</th>
          <th>创建时间</th>
          <th>更新时间</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="user in userList" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.realName }}</td>
          <td>{{ user.age }}</td>
          <td>{{ user.sex === 0 ? '女' : '男' }}</td>
          <td>
              <span :class="{
                'status-active': user.isAccountNonExpired && user.isAccountNonLocked && user.isCredentialsNonExpired && user.enabled,
                'status-inactive': !(user.isAccountNonExpired && user.isAccountNonLocked && user.isCredentialsNonExpired && user.enabled)
              }">
                {{ user.isAccountNonExpired && user.isAccountNonLocked && user.isCredentialsNonExpired && user.enabled ? '正常' : '异常' }}
              </span>
          </td>
          <td>
            <div class="permission-codes">
              <span v-for="(permission, index) in user.permissionList" :key="permission.id" class="permission-code">
                {{ permission.code }}
                <span v-if="index < user.permissionList.length - 1" class="separator">, </span>
              </span>
              <span v-if="!user.permissionList || user.permissionList.length === 0" class="no-permission">无权限</span>
            </div>
          </td>
          <td>{{ formatDate(user.createTime) }}</td>
          <td>{{ formatDate(user.updateTime) }}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <div v-if="userList.length === 0" class="empty-state">
      <p>暂无用户数据</p>
    </div>
    <button @click="handleLogout" class="logout-btn">退出登录</button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const userList = ref<any[]>([]);

// 日期格式化
const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });
};

// 获取用户列表
const fetchUserList = async () => {
  try {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      alert('请先登录');
      await router.push('/login');
      return;
    }

    const response = await axios.get('/api/users/list', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    userList.value = response.data.data;
  } catch (error: any) {
    console.error('获取用户列表失败:', error);
    if (error.response?.status === 401) {
      localStorage.removeItem('jwtToken');
      alert('登录已过期，请重新登录');
      await router.push('/login');
    } else {
      alert('获取用户列表失败，请稍后重试');
    }
  }
};

// 退出登录
const handleLogout = async () => {
  try {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      localStorage.removeItem('jwtToken');
      await router.push('/login');
      return;
    }

    // 发送退出登录请求到后端
    const response = await axios.post(
        '/api/logout',
        {},
        {
          headers: {
            'Authorization': `Bearer ${token}`
          },
          withCredentials: true
        }
    );

    // 处理后端返回的响应
    console.log('退出登录响应:', response.data);
    if (response.data?.message === 'success') {
      // 收到success消息后，清除本地token并跳转登录页
      localStorage.removeItem('jwtToken');
      console.log('token已清除，返回登录页');
      await router.push('/login');
    } else {
      // 处理退出失败的情况
      console.error('退出登录失败:', response.data?.message);
      alert('退出登录失败，请稍后重试');
    }
  } catch (error: any) {
    console.error('退出登录请求错误:', error);
    // 网络错误或其他异常时，也清除本地token并跳转
    localStorage.removeItem('jwtToken');
    await router.push('/login');
  }
};

// 组件挂载时获取用户列表
onMounted(() => {
  fetchUserList();
});
</script>

<style scoped>
.permission-codes {
  max-width: 200px;
  word-wrap: break-word;
}

.permission-code {
  display: inline-block;
  padding: 2px 5px;
  background-color: #f0f0f0;
  border-radius: 3px;
  margin: 2px 0;
}

.separator {
  margin: 0 2px;
}

.no-permission {
  color: #999;
  font-style: italic;
}

.user-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.page-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 28px;
}

.user-table-wrapper {
  overflow-x: auto;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fff;
}

.user-table th,
.user-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.user-table th {
  background-color: #f5f7fa;
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.user-table td {
  color: #666;
  font-size: 14px;
}

.user-table tr:hover {
  background-color: #fafafa;
}

.status-active {
  color: #52c41a;
  font-weight: 500;
}

.status-inactive {
  color: #f5222d;
  font-weight: 500;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.logout-btn {
  display: block;
  margin: 30px auto 0;
  padding: 10px 30px;
  background-color: #ff4d4f;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.logout-btn:hover {
  background-color: #ff7875;
}

.logout-btn:active {
  background-color: #d9363e;
}
</style>
