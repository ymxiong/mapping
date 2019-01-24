# Mapping

中文地址移步:

> https://github.com/ymxiong/Mapping/blob/master/README_zh.md

## Target

Using `@Mapper`  `@MapperIgnore`  `@MapperRename`  `@MapperModify`  and `@MapperExtra`  annotations to simplify your entity system. Those annotations can work in with each other, using to modify, transform, and bundle entity attribute information, which is easy to provide to front-end developer for revealing, and also facilitates background developer's management.

## Installation

Add the dependency to your pom.xml

```xml
<dependency>
    <groupId>cc.eamon.open</groupId>
    <artifactId>mapping</artifactId>
    <version>${mapping-version}</version>
</dependency>
```

## Usage

### Start Up

1. Add `@Mapper` annotation to your entity，and create get&set methods of your properties（You can work in with lombok)

   ```java
   @Mapper
   public class User {
       private int id;
   	...
   }
   ```

2. Complie your project, the `UserDefaultMapper` will be automatically generated, which include`Map<> getMap(User obj)` and`User getEntity()` two main method.

   ```java
   public class UserDefaultMapper {
       public int id;
       /**
       * Static Method, Invoke Directly
       */
       public static Map<String, Object> getMap(User obj) {
           Map<String, Object> resultMap = new LinkedHashMap<>();
           if (obj == null) return resultMap;
           resultMap.put("id", obj.getId());
           return resultMap;
       }
       /**
       * Using After Instantiate Mapper
       */
       public User getEntity() {
           User entity = new User();
           entity.setId(this.id);
           return entity;
       }
   }
   ```