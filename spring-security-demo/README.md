# spring security tutorial

## 1. create a simple spring boot application

github: [https://github.com/yanglimou/spring-security-demo/tree/release-1](https://github.com/yanglimou/spring-security-demo/tree/release-1)

use spring initializr to create a simple spring boot applicaiton with just a spring-boot-starter-web library

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-security-demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-security-demo</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>


        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

add a HomeController to expose three api for calling

```java
package com.example.springsecuritydemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "<h1>home</h1>";
    }
    @GetMapping("/user")
    public String user(){
        return "<h1>user</h1>";
    }
    @GetMapping("/admin")
    public String admin(){
        return "<h1>admin</h1>";
    }
}
```
after that, we can run the application and call the apis above from browser. and you can get success responsees

## 2. add spring security to spring boot application

github: [https://github.com/yanglimou/spring-security-demo/tree/release-2](https://github.com/yanglimou/spring-security-demo/tree/release-2)

update the pom.xml and add spring security library like below

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

restart the application and call those api above. then you will get a login page where you need to input you appropriate username and password and click login button to login before you get success response.

the username is user and the password was logged in the console when the application was starting.

like this

> Using generated security password: 1ab4543c-47e6-4454-b4dd-c996c01dc972

By default, the spring security will add these below to our application

- intercept all out custom apis before you authencation you account
- add a user with username 'user' and random password in console
- add a login page that you will redirect when you don't logged in. and you can input you username and password and send a login request
- add a login api that you can log in
- add a logout api that you can log out

## 3. set up authentication in configuration file

github: [https://github.com/yanglimou/spring-security-demo/tree/release-3](https://github.com/yanglimou/spring-security-demo/tree/release-3)

we can set up spring security authentication with username and password in application.yaml like below

```yaml
spring:
  security:
    user:
      name: user
      password: user
```

then, we'd input appropriate username and password that setted up in the yaml file above in login page before we pass to the resources page like '/','/user','/admin'.

## 4. set up authentication in java file

github: [https://github.com/yanglimou/spring-security-demo/tree/release-4](https://github.com/yanglimou/spring-security-demo/tree/release-4)

in this section, we will learn to set up authentication in spring security configuration with inMemoryAuthentication.

- add a java class extends WebSecurityConfigurerAdapter
- override the configure(AuthenticationManagerBuilder auth) method
- expose a PasswordEncoder with @bean Annotation

```java
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("user")
                .roles("USER");

    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}

```

- expose this spring security configuration file with @EnableWebSecurity Annotation

at now, we expose a PasswordEncoder called NoOpPasswordEncoder that will not encoder the password, so we can test the application easily. But for security, we'd choose another PasswordEncoder like BCryptPasswordEncoder in production environment.

## 5. set up authorization in java file

github: [https://github.com/yanglimou/spring-security-demo/tree/release-5](https://github.com/yanglimou/spring-security-demo/tree/release-5)

besides we can set up authencation, we can also set up authorization to describe the relationship between user and api. some user with 'USER' role and access '/user' api, and other user with 'ADMIN' role can access '/user' and '/admin' apis. 

to achieve this goal, we'd override the configure(AuthenticationManagerBuilder auth) method in out custom java class above

```java
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("user")
                .roles("USER")
                .and()
                .withUser("admin")
                .password("admin")
                .roles("ADMIN","USER");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/user").hasAnyRole("ADMIN","USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .and().formLogin();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
```

## 6. set up authentication with jdbc

github: [https://github.com/yanglimou/spring-security-demo/tree/release-6](https://github.com/yanglimou/spring-security-demo/tree/release-6)

in reality, we use database to store user message, and so we should authencate our logging user with databse. to achieve this, we can change inMemoryAuthencation to JdbcAuthencation.

here, we use mysql database to store user message. 

- add tables in mysql

```sql
-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '123', '123', '0');
INSERT INTO `users` VALUES ('2', 'admin', 'admin', '1');
INSERT INTO `users` VALUES ('3', 'user', 'user', '1');

-- ----------------------------
-- Table structure for authorities
-- ----------------------------
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of authorities
-- ----------------------------
INSERT INTO `authorities` VALUES ('1', '123', 'ROLE_USER');
INSERT INTO `authorities` VALUES ('2', 'admin', 'ROLE_ADMIN');
INSERT INTO `authorities` VALUES ('3', 'admin', 'ROLE_USER');
INSERT INTO `authorities` VALUES ('4', 'user', 'ROLE_USER');
```

- add dependencies association with database.

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.48</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

- set up database's connection message in application.yaml

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/spring_security_demo?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
```

- update configure(AuthenticationManagerBuilder auth) method from inMemoryAuthencation to JdbcAuthencation

```java
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled from users where username = ?")
                .authoritiesByUsernameQuery("select username,authority from authorities where username = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/user").hasAnyRole("ADMIN","USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .and().formLogin();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
```

## 7. set up authentication with jpa

github: [https://github.com/yanglimou/spring-security-demo/tree/release-7](https://github.com/yanglimou/spring-security-demo/tree/release-7)

In large project, we may use persistence framework like 'hibernate','mybatis' or 'jpa'. here we use jpa to connect with database. the UserDetailsService just contains one method called loadUserByUsername(String s). in this method we can use ourself logic to query user message. then pay attention to this method's return value. it's a UserDetails object. we should add a class implement UserDetails.we can use our UserDetails class to contain our user message.

- update dependencies

remove spring-boot-starter-data-jdbc and add spring-boot-starter-data-jpa

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

- update configure(AuthenticationManagerBuilder auth)

inject a UserDetailsService for AuthenticationManagerBuilder to use. then we provider a custom UserDetailsService for query user message in database.

```java
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/user").hasAnyRole("ADMIN", "USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .and().formLogin();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

- add a java class impletement UserDetailsService interface



```java
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users user = usersRepository.getByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("username not found: " + s);
        }
        return new MyUserDetails(user);
    }
}
```

```java
public class MyUserDetails implements UserDetails {
    private Users users;

    public MyUserDetails(Users users) {
        this.users = users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return users.getAuthoritiesList().stream()
                .map(authorities -> authorities.getAuthority())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return users.getEnabled().intValue() == 1;
    }
}
```

- add Users/Authorities entity and UserRepository

```java
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
	//Getter/Setter
}
```

```java
@Entity
public class Authorities implements Serializable {
    @Id
    private Long id;
    private String username;
    private String authority;
    //Getter/Setter
}
```

```java
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users getByUsername(String s);
}
```

