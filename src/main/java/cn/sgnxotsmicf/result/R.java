package cn.sgnxotsmicf.result;

import lombok.*;

/**
 * @Author: lixiang
 * @CreateDate: 2026/1/4 17:20
 * @Version: 1.0
 * @Description: 通用返回结果类
 */

@Builder //Builder模式构建对象，为了解决不允许调用者随意setter或者随意在构造函数乱注入数据的问题
@NoArgsConstructor //添加无参构造方法，为了防止特殊场景下报错如反射转Json时报错
@AllArgsConstructor
@Getter
public class R<T> {

     /**
      * 状态码
      */
     private StateCode stateCode;

     /**
      * 提示信息
      */
     private String message;

     /**
      * 数据
      */
     private T data;


     /**
      * 操作成功，自定义提示信息
      * @param message 提示信息
      * @return R<T>
      */
     public static <T> R<T> success(String message) {
         return R.<T>builder()
                 .stateCode(StateCode.SUCCESS)
                 .message(message)
                 .build();
     }

     /**
      * 操作成功，自定义数据
      * @param data 数据
      * @return R<T>
      */
     public static <T> R<T> success(T data) {
         return R.<T>builder()
                 .stateCode(StateCode.SUCCESS)
                 .message("success")
                 .data(data)
                 .build();
     }


     /**
      * 操作成功，自定义提示信息和数据
      * @param message 提示信息
      * @param data 数据
      * @return R<T>
      */
     public static <T> R<T> success(String message, T data) {
         return R.<T>builder()
                 .stateCode(StateCode.SUCCESS)
                 .message(message)
                 .data(data)
                 .build();
     }


     /**
      * 操作成功，默认提示信息为"操作成功"
      * @return R<T>
      */
     public static <T> R<T> success() {
         return success(null);
     }

     /**
      * 操作失败，自定义提示信息
      * @param message 提示信息
      * @return R<T>
      */
     public static <T> R<T> fail(String message) {
         return R.<T>builder()
                 .stateCode(StateCode.FAIL)
                 .message(message)
                 .data(null)
                 .build();
     }

     /**
      * 操作失败，默认提示信息为"操作失败"
      * @return R<T>
      */
     public static <T> R<T> fail() {
         return fail("fail");
     }
}
