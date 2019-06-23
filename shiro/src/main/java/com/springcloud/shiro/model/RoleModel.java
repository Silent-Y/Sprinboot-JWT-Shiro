package com.springcloud.shiro.model;

import java.util.List;

public class RoleModel {

    private Long id;

    private String name;

    private List<PermissionModel> permissionModels;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PermissionModel> getPermissionModels() {
        return permissionModels;
    }

    public void setPermissionModels(List<PermissionModel> permissionModels) {
        this.permissionModels = permissionModels;
    }

    @Override
    public String toString() {
        return "RoleModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permissionModels=" + permissionModels +
                '}';
    }
}
