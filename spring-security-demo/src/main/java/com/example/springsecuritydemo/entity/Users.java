package com.example.springsecuritydemo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Users implements Serializable {
    @Id
    private Long id;
    private String username;
    private String password;
    private Integer enabled;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private List<Authorities> authoritiesList;

    public List<Authorities> getAuthoritiesList() {
        return authoritiesList;
    }

    public void setAuthoritiesList(List<Authorities> authoritiesList) {
        this.authoritiesList = authoritiesList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
}
