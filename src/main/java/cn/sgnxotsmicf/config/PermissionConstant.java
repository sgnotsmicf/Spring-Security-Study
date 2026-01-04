package cn.sgnxotsmicf.config;

import lombok.Getter;

@Getter
public enum PermissionConstant {
    ADMIN("admin","管理员"),
    USER("user","普通用户"),
    GUEST("guest","访客"),
    SUPER_ADMIN("manager","销售经理");

    private final String role;
    private final String description;

    PermissionConstant(String role, String description) {
        this.role = role;
        this.description = description;
    }


}
