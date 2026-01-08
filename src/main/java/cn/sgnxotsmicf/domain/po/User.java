package cn.sgnxotsmicf.domain.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * 
 * </p>
 *
 * @author sgnxotsmicf
 * @since 2025-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
public class User implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    @JsonIgnore // 密码不参与序列化返回
    private String password;

    /**
     * 姓名
     */
    @TableField("name")
    private String realName;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 0-女 1-男
     */
    @TableField("sex")
    private Integer sex;


    /**
     * 账号是否过期
     */
    @TableField("account_no_expired")
    private Integer isAccountNonExpired;

    /**
     * 账号是否过期
     */
    @TableField("account_no_locked")
    private Integer isAccountNonLocked;


    /**
     * 密码是否过期
     */
    @TableField("credentials_no_expired")
    private Integer isCredentialsNonExpired;

    /**
     * 是否启用
     */
    @TableField("account_enable")
    private Integer isEnabled;

    /**
     * 创建时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // timezone 时区，默认是 GMT 时区，需要设置为东八区
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // timezone 时区，默认是 GMT 时区，需要设置为东八区
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 被谁创建
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 逻辑删除，1代表删除，0代表未删除
     */
    @TableField("is_deleted")
    @TableLogic(value = "0", delval = "1")  // value: 0 代表未删除，1 代表删除
    private Integer isDeleted;

    /**
     * 角色列表
     */
    @JsonIgnore
    @TableField(exist = false)
    private List<Role> roleList;

    /**
     * 权限列表
     */
    //@JsonIgnore
    @TableField(exist = false)
    private List<Permission> permissionList;

    //返回用户的权限(角色、code)
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //这里面最关键的是role.getRole()，role.getRole()是角色的code，例如：ROLE_ADMIN、ROLE_USER等
        //new SimpleGrantedAuthority(role.getRole())是将角色的code转换为 GrantedAuthority 对象
        //.toList()是将 Stream 转换为 List

        //角色管理控制
        //return roleList.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).toList();//"ROLE_"
        //权限管理控制
        return permissionList.stream().map(permission -> new SimpleGrantedAuthority(permission.getCode())).toList();
        //return List.of();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (this.isAccountNonExpired != null) {
            return this.isAccountNonExpired == 1;
        }
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (this.isAccountNonLocked != null) {
            return this.isAccountNonLocked == 1;
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (this.isCredentialsNonExpired != null) {
            return this.isCredentialsNonExpired == 1;
        }
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (this.isEnabled != null) {
            return this.isEnabled == 1;
        }
        return true;
    }
}