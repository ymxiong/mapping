# Mapping

实体关系映射配置化生成框架。

过去几年里，微服务架构逐渐兴起，互联网数字化进程发展迅速。快速完成服务间实体沟通，前后台模型沟通是重要的基础能力建设。

Mapping框架通过自动生成映射代码，实现实体关系的配置化打通。

## 功能目录

提供`@Mapper` `@MapperIgnore` `@MapperRename` `@MapperModify` `@MapperExtra` `@MapperConvert`和`@MapperDoc`注解简化实体控制系统，这些注解可配合使用，用于修饰、改造、捆绑实体属性信息、用于提供给前端人员处理，也用于后台配置式管理和服间打通。

### 类注解

+ @Mapper
+ @MapperExtra
+ @MapperConvert
+ @MapperDoc
+ @Mapping（FUTURE）

### 域注解

+ @MapperIgnore
+ @MapperRename
+ @MapperModify

###方法注解（FUTURE）



## 安装

添加依赖库至 pom.xml

```xml
<dependency>
    <groupId>cc.eamon.open</groupId>
    <artifactId>mapping</artifactId>
    <version>${mapping-version}</version>
</dependency>
```

## 使用说明

### 开始

1. 在实体上添加 `@Mapper` 注解，并为属性添加get、set方法（可配合lombok使用）。

   ```java
   @Mapper
   public class User {
       private int id;
   	...
   }
   ```

2. 编译项目, 系统会自动生成 `UserDefaultMapper` ，包含`Map<> getMap(User obj)`和`User getEntity()`两个主要方法。

   ```java
   public class UserDefaultMapper {
       public int id;
       /**
       * 静态方法，可直接调用
       */
       public static Map<String, Object> getMap(User obj) {
           Map<String, Object> resultMap = new LinkedHashMap<>();
           if (obj == null) return resultMap;
           resultMap.put("id", obj.getId());
           return resultMap;
       }
       /**
       * 需要实例化Mapper后使用
       */
       public User getEntity() {
           User entity = new User();
           entity.setId(this.id);
           return entity;
       }
   }
   ```

3. getMap方法用于将实体转化成Map映射，getEntity方法用于从Mapper中生成实体信息。

4. 此时基本用法已经清晰：

   - Mapper类可作为前端上传使用的json对象，传到后台的信息通过getEntity()生成实体。
   - 后台返回信息时调用getMap()，获得根据配置映射的返回结果。



### Ignore

场景：前端进行post请求的时候，我们通常不希望前端上传实体id，实体id由后台管理生成并返回给前端。通常的做法是手写Map信息将上传所需要的属性一一对应。在这里使用@MapperIgnore注解可以轻松解决此问题。

1. 为Mapper注解添加post参数，将自动生成 `UserPostMapper`。

2. 在实体中为id字段标注 `@MapperIgnore` 注解， `UserPostMapper` 将自动忽略id字段。

   ```java
   @Mapper("post")
   public class User {
       @MapperIgnore("post")
       private int id;
   	...
   }
   ```

3. 通过`userPostMapper.getEntity()` 获取到的实体信息和通过 `UserPostMapper.getMap(User obj)` 获取到的映射信息都将忽略id属性。

### Rename

场景1：需要将服务提供给多个业务，业务字段命名不统一，映射起来会十分麻烦。

场景2：需变更对外展示属性名，但业务逻辑上与数据库命名保持一致。

1. 在实体中为需要改名字段标注 `@MapperRename` 注解， 对应的Mapper将自动改名。

   ```java
   @Mapper({"srv1", "srv2"})
   public class User {
   	...
       @MapperRename(value = {"srv1", "srv2"}, name = {"mobile", "tel"})
       private String phone;
   
   }
   ```

2. phone 字段在srv1中将被重命名为mobile，在srv2中将被重命名为tel， 在映射的时候变更属性名，保证在业务流转操作中保持名称一致。



### Modify

场景1：身份证，手机号等身份信息返回前端时需模糊掉部分信息（如：'10123456789' => '101\*\*\*\*6789' ）。

场景2：保持时间信息一致性，时间以时间戳方式上传。

1. 在实体中添加修改和恢复方法

2. 在实体中为需要改名字段标注 `@MapperModify` 注解

   ```java
   @00Mapper({"srv1", "srv2", ...})
   public class User {
       ...
           
       @MapperRename(value = {"srv1", "srv2"}, name = {"mobile", "tel"})
       @MapperModify(
           value = "srv1", 
           modify = "convertPhone", 
           recover = "")
       private String phone;
   
       @MapperModify(
           value = "srv2", 
           modify = "convertUpdateTime", 
           recover = "setLongUpdateTime")
       private Date updateTime;
   
       public String convertPhone() {
           if (this.phone == null) return "";
           return this.phone.substring(0, 3) + "****" + this.phone.substring(7);
       }
   
       public void setLongUpdateTime(long time){
           this.updateTime = new Date(time);
       }
   
       public Long convertUpdateTime(){
           if (this.updateTime == null) return null;
           return this.updateTime.getTime();
       }
   
   }
   ```

3. phone 字段在srv1中将被格式化，updateTime在srv2中将被修改为Long类型。

### 小结

通过以上阶段处理，实体配置信息如下：

```java
@Setter
@Getter
@Mapper({"post", "srv1", "srv2"})
public class User {

    @MapperIgnore("post")
    private int id;

    @MapperRename(
        value = {"srv1", "srv2"}, 
        name = {"mobile", "tel"})
    @MapperModify(
        value = "srv1", 
        modify = "convertPhone", 
        recover = "")
    private String phone;

    @MapperModify(
        value = "srv2", 
        modify = "convertUpdateTime", 
        recover = "setLongUpdateTime")
    private Date updateTime;

    public String convertPhone() {
        if (this.phone == null) return "";
        return this.phone.substring(0, 3) + "****" + this.phone.substring(7);
    }

    public void setLongUpdateTime(long time){
        this.updateTime = new Date(time);
    }

    protected Long convertUpdateTime(){
        if (this.updateTime == null) return null;
        return this.updateTime.getTime();
    }

}
```

打印四组Mapper信息如下：

1. DefaultMapper，完整信息

   > {id=1, phone=10123456789, updateTime=Wed Jan 02 15:20:32 CST 2019}

2. PostMapper，忽略id

   > {phone=10123456789, updateTime=Wed Jan 02 15:12:34 CST 2019}

3. Srv1Mapper，phone改名为mobile

   > {id=1, mobile=1016789, updateTime=Wed Jan 02 15:12:34 CST 2019}

4. Srv2Mapper，phone改名为tel，updateTime改类型为Long

   > {id=1, tel=10123456789, updateTime=1546413154309}

即通过配置的方式完成了对实体信息的改进或者增强。



### 扩展使用：Extra

场景：在更新用户的同时，同时更新用户的权限列表，此时需要给User实体加上权限列表属性

配置如下：

```java
@Setter
@Getter
@Mapper("update")
@MapperExtra(
        value = "update", // 对应的mapper
        list = true, // 是否为list
        name = "permits", // 属性名称
        type = "java.lang.Integer"// 对象类型
)
public class User {

    private int id;

}
```

生成结果：

```java
public class UserUpdateMapper {
    
    public int id;
    
    public List<Integer> permits;
    
    public static Map<String, Object> getMap(User obj) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        if (obj == null) return resultMap;
        resultMap.put("id", obj.getId());
        return resultMap;
    }
    public static Map<String, Object> getMapWithExtra(User obj, List<Integer> permits) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        if (obj == null) return resultMap;
        resultMap.put("id", obj.getId());
        resultMap.put("permits", permits);
        return resultMap;
    }
    public User getEntity() {
        User entity = new User();
        entity.setId(this.id);
        return entity;
    }
}
```

系统自动生成getMapWithExtra()方法，参数列表中多出extra字段。

## 开发说明

### 创建编译时注解

1. @Mapper 只需一个value参数，有多少参数既生成多少张Mapper
2. @MapperIgnore 用于忽略指定属性。只需一个value参数，value参数用于设置作用于哪张mapper。
3. @MapperRename 用于属性改名。需value与name参数，value参数用于设置作用于哪张mapper，name参数用于确定改名后名称信息。
4. @MapperModify 用于属性类型与值的修订。需value、modify与recover参数，value参数用于设置作用于哪张mapper，modify参数用于确定修改参数使用的方法，recover参数用于确定用于恢复修改参数的方法。
5. @MapperExtra 用于给类型增加额外信息。需value、name、type与list参数，value参数用于设置作用于哪张mapper，name参数用于确定额外信息的名称，type信息用于确定额外信息的类型，list信息用于确定额外信息是否为数组。



### 处理编译时注解

1. 建立 `MapperProcessor` 注解处理器，该处理器只处理有Mapper注解的类，生成与注解配置对应的Mapper。
2. 遇到MapperIgnore字段忽略此字段
3. 遇到MapperRename字段进行改名，同时写出恢复信息。
4. 遇到MapperModify字段进行字段编辑，生成和恢复时调用制定方法。
5. 遇到MapperExtra字段进行数据联合，将其他类引入当前注解处理过程中。
6. 遇到MapperGroup注解时将生成的类加上@Group注解。



### 写出文件

1. 写出经过 `MapperIgnore` `MapperRename` `MapperModify` 处理后的属性域信息
2. 整合`MapperExtra` 提及的整合信息
3. 域信息写入getMap() 方法
4. 额外域信息写入getMapExtra() 方法
5. 域转换信息写入getEntity() 方法
6. 写出以上方法