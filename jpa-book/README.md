# JPA

### 양방향 연관관계의 주인

```java

@Entity
public class Member {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "USERNAME")
  private String username;

  @ManyToOne
  @JoinColumn(name = "TEAM_ID")
  private Team team;

  public void setTeam(Team team) {
    this.team = team;
  }
}

@Entity
public class Team {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @OneToMany(mappedBy = "team")
  private final List<Member> members = new ArrayList<>();

  public void addMember(Member member) {
    member.setTeam(this);
    members.add(member);
  }
}
```

위와 같이 `Member`와 `Team`은 N:1 관계이다. 즉, `Team`은 여러 `Member`를 가질수 있지만, `Member`는 하나의 `Team`만 가질 수 있다. 그러므로 `Member`과 `Team`은
양방향 연관관계이다.

## mappedBy

### 객체의 연관관관계는 2개

- 회원 -> 팀 연관관계 1개 (단방향)
- 팀 -> 회원 연관관계 1개 (단방향)

객체의 **양방향 관계는 사실 양방향 관계가 아니라 서로 다른 단방향 관계 2개다.**

### 테이블의 연관관계는 1개

- 회원 <-> 팀 연관관계 1개 (양방향)

테이블은 **외래키 하나**로 양방향 관계를 가진다.

`Member.getTeam()`과 `Team.getMembers()`로 서로를 참조 하다보니까 객체의 양방향 관계에서 데이터를 수정하려고 할때, `Member.team`과 `Team.members` 중 어느 것을
수정해야하는지에 대한 딜레마가 발생한다.
하지만 DB 입장에서는 `Member`의 `Team.id`값만 변경하면 된다. 그래서 `mappedBy`를 사용하여 연관관계의 주인을 정해주어야 한다.

### 연관관계의 주인 (Owner)

양방향 매핑 규칙

- 객체의 두 관계중 하나를 연관관계의 주인으로 지정
- **연관관계의 주인만이 FK(외래 키)를 관리(등록, 수정)**
- **주인이 아닌쪽은 읽기만 가능**
- 주인은 `mappedBy` 속성 사용 X
- 주인이 아니면 `mappedBy` 속성으로 주인 지정

### 누구를 주인으로?

- 외래키가 있는 곳을 주인으로 정해라
- 여기서는 `Member.team`, 즉 `Member`, 1:N쪽에서 N쪽이 주인이다.

## 상속 관계

JPA는 3가지 상속관계를 지원한다.

- 조인 전략: 부모 클래스와 자식 클래스를 각각 테이블로 만들고 조인으로 조회한다.
- 단일 테이블 전략: 부모 클래스와 자식 클래스를 하나의 테이블로 만들고 구분 컬럼으로 조회한다.
- 구현 클래스마다 테이블 전략: 부모 클래스와 자식 클래스를 각각 테이블로 만들고 자식 테이블에 부모 테이블의 PK를 받는다.

아무 설정 안할시 단일 테이블 전략이 기본값이 된다.

```java

@Entity
class Item {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private int price;
}

// 아무런 설정없이 단순치 상속 시킬 경우, 단일 테이블 전략이 기본값이 된다.
@Entity
class Album extends Item {
  private String artist;
}
```

---

### 조인 전략 (JOINED)

조인 전략은 부모 클래스와 자식 클래스를 각각 테이블로 만들고 조인으로 조회한다. 부모 클래스에 `@Inheritance(strategy = InheritanceType.JOINED)`를 설정하면 된다.
그러면 상속 받는 자식 클래스는 부모 클래스의 PK를 FK로 받는다. 그래서 자식 엔티티를 조회하면 부모 엔티티와 조인해서 조회한다. 생성할때도 자식 엔테테를 생성해서 DB에 쿼리를 보내게 되면, 부모, 자식
테이블에 각각 데이터가 들어가게 된다.

```java

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략
class Item {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private int price;
}
```

이때 `DTYPE` 컬럼이 생성되는데, 이 컬럼은 부모, 자식 테이블을 구분하기 위한 컬럼이다. 부모 엔티티에 `@DiscriminatorColumn`을 설정하면 `DTYPE` 컬럼의 이름을 변경할 수 있다.

```java

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략
@DiscriminatorColumn
class Item {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private int price;
}
```

이때 `Movie` 데이터를 생성하게 되었을때 DB에는 다음과 같이 데이터가 생성된다.

| id | DTYPE | name | price |
|----|-------|------|-------|
| 1  | Movie | 예시   | 예시    |

`DiscriminatorColumn`을 설정하지 않아도 되지만, DB의 관점에서 볼때 어느 자식 엔티티에서 생긴 데이터를 구분할수 있기 위해서 있는게 좋다.

`DiscrimiatorValue`를 설정하면 부모 엔티티의 `DTYPE` 컬럼에 들어가는 값이 변경된다.

```java

@Entity
@DiscriminatorValue("M")
class Movie extends Item {
  private String director;
}
```

이렇게 설정하면 DType의 값은 `M`이 된다.

### 장점

- 데이터 정규화가 되어 있다.
- 외래키 참조 무결성 제약조건을 활용할 수 있다.
- 저장공간을 효율적으로 사용할 수 있다.

### 단점

- 조회시 조인을 많이 사용하므로, 성능이 저하될 수 있다.
- 조회 쿼리가 복잡하다.
- 데이터 저장시 INSERT SQL이 두번 발생한다.

대부분의 상황에서는 조인 전략이 가장 많이 사용된다.

---

## 단일 테이블 전략 (SINGLE_TABLE)

단일 테이블 전략은 부모 클래스와 자식 클래스를 하나의 테이블로 만들고 구분 컬럼으로 조회한다. 부모 클래스에 `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`를
설정하거나 아무 설정을 하지 않고 상속 관계를 만들면 된다.
이때 `DType`은 필수적으로 필요하다.

```java

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 단일 테이블 전략
class Item {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private int price;
}
```

3가지 전략중 성능이 제일 우수하다. 하지만 단일 테이블 전략은 테이블이 커지면 성능이 떨어진다. 그러므로 상황에 맞게 사용해야 한다.

### 장점

- 조인의 필요 없으므로 일반적으로 조회 성능이 빠르다.
- 조회 쿼리가 단순하다.

### 단점

- 자식 엔티티가 매칭한 컬럼은 모두 null이 허용된다.
- 단일 테이블에 모든 것을 저장하므로, 데이블이 커지게 될때 조회 성능이 오히려 느려질수 있다.

---

## 구현 클래스마다 테이블 전략 (TABLE_PER_CLASS)

구현 클래스마다 테이블 전략은 부모 클래스의 속성들을 모두 자식 클래스에 넣어서 자식 클래스마다 테이블을 만든다. 부모
클래스에 `@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)`를 설정하면 된다.

```java

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 구현 클래스마다 테이블 전략
abstract class Item {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private int price;
}
```

이때 `Item`을 상속받는 `Album`과 `Movie`가 있다고 가정하면, `Item`의 속성들이 `Album`과 `Movie`에 모두 들어가게 된다. 그러므로 `Item`의 속성들이 중복되어 들어가게 된다.
그리고 `Item`을 추상 클래스로 만들어야한다. 그 이유는 `Item` 클래스는 단독으로 사용하거나 인스턴스를 만들어선 안되기 때문이다.
그리고 이 전략은 사용하면, `@DescriminatorColumn`과 `@DescriminatorValue`를 사용할 필요가 없다. 왜냐하면, 부모 클래스의 속성들이 자식 클래스에 모두 들어가기 때문이다.

**이 전략은 DevOps와 ORM 전문가 둘다 추천하지 않는 전략이다.**

변경에 대하 유연하지 못하는 전략이다.

### 장점

- 서브 타입을 명확하게 구분해서 처리할때 효과적
- not null 제약조건 사용 가능

### 단점

- 여러 자식 테이블을 함꼐 조회할때 성능이 느림 (Union SQL)
- 자식 테이블을 통합해서 쿼리하하기 어려움

## 고급 매핑 (상속 관계)

### @MappedSuperclass

`@MappedSuperclass`는 부모 클래스를 상속 받는 자식 클래스에게 매핑 정보만 제공하고 실제 테이블과 매핑되지 않는다. 즉, 부모 클래스는 테이블과 매핑되지 않고 자식 클래스에게 매핑 정보만 제공한다.

```java

@MappedSuperclass
class BaseEntity {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String description;
}

@Entity
class Item extends BaseEntity {

  private int price;
}
```

이런식으로 `BaseEntity`에 `@MappedSuperclass`를 설정하면, `Item`에 `BaseEntity`의 속성들이 들어가게 된다. 그리고 DB에는 다음과 같은 테이블이 만들어진다.

### Item

| id | name | description | price |
|----|------|-------------|-------|

### 특징

- 상속관계 매핑 X
- 엔티티 X, 테이블과 매핑 X
- 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
- 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
- `@Entity` 클래스는 엔티티나 `@MappedSuperclass`로 지정한 클래스만 상속 가능
- 인스턴스를 만들 일이 없으므로, 추상 클래스로 만드는 것을 권장한다.

## 프록시 (Proxy)

프록시 객체는 Hibernate가 제공하는 기능으로, 실제 엔티티 객체를 `target`으로 하는 가짜 객체를 만드는 것을 의미한다. 프록시 객체는 실제 엔티티 객체를 상속해서 만들고 있으며, 실제 엔티티의 메소드가
필드에 접근하기 전, 접근을 가로채 영속성 컨텍스트에 데이터가 있으면 바로 반환하고, 없으면 DB에서 조회를 요청하여, 프록시 객체가 실제 엔티티 객체와 연결이 되면, 실제 엔티티의 메소드와 필드를 불러서 데이터를
반환하는 역할을 한다.

### 특징

- 프록시 객체는 처음 사용할 때 한 번만 초기화
- 프록시 객체를 초기화할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아니다. 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근이 가능하다.
- 프록시 객체는 원본 엔티티를 상속 받음, 따라서 타입 체크시 주의해야한다(`==` 비교를 하면 실패한다. 대신 `instanceof`이나 코틀린에서는 `is`키워드를 사용).
- 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 `em.getReference()`를 호출해도 실제 엔티티가 반환된다.
- 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태(영속성 컨텍스트에서 관리 받지 않는 상태)일 때, 프록시를 초기화하면 문제 발생 (hibernate는 `LazyInitializationException`
  에외를 터트린다.)

### 프록시 확인

JPA(혹은 Hibernate)에서 프록시 객체들을 위한 편의 기능이 있다.

- 프록시 인스턴스의 초기화 여부 확인
  `PersistenceUnitUtil.isLoaded(Object entity)`
- 프록시 클래스 확인 방법
  `entity.getClass().getName()` 출력. 결과에서 `$HibernateProxy`가 있으면 프록시 객체임을 의미
- 프록시 강제 초기화
  `Hibernate.initialize(entity)`

참고로, JPA 표준에는 강제 초기화가 없지만 하지만 강제 호출은 있다. `entity.getterMethod()`이런식으로 호출하면 강제로 호출할수 있다.

## 즉시 로딩과 지연 로딩

`@ManyToOne` 같은 연관관계 어노테이션에 `fetch` 속성을 통해 즉시 로딩과 지연 로딩을 설정할수 있다.
`FetchType.EAGER`는 즉시 로딩, `FetchType.LAZY`는 지연 로딩이다.

`FetchType.Lazy`로 설정한 경우, 연관관계 객체가 프록시 객체로 받는 것을 알수 있다.

## 영속성 전이:CASCADE

- 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶을때 사용함
- 예: 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장
- `CascadeType.ALL`: 모두 적용
- `CascadeType.PERSIST`: 영속
- `CascadeType.REMOVE`: 삭제
- `CascadeType.MERGE`: 병합
- `CascadeType.DETACH`: 준영속
- `CascadeType.REFRESH`: REFRESH
- 주의: `CascadeType.REMOVE`는 연관된 엔티티도 함께 삭제(삭제 시점에 실제로 삭제됨)
- 주의: 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없다.
- 주의: 엔티티를 영속화할 때 연관된 엔티티도 한께 영속화하는 편리함을 제공할 뿐이다.

```java

@Entity
public class Parent {
  @Id
  @GeneratedValue
  private Long id;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private final List<Child> children = new ArrayList<>();
}

@Entity
public class Child {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PARENT_ID")
  private Parent parent;
}
```

## 고아 객체

고아 객체란 부모 엔티티와 연관관계가 끊어진 자식 엔티티 객체를 의미한다.

- `orphanRemoval = true`를 설정하면, 부모 엔티티와 연관관계가 끊어진 자식 엔티티 객체를 자동으로 삭제한다.

### 고아 객체 - 주의

- 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능이다
- **참조하는 곳이 하나일 때 사용해야함!**
- **특정 엔티티가 개인 소유할 때 사용**
- `@OneToOne`, `@OneToMany`만 가능

참고: 개념적으로 부모를 제거하면 자식을 고아가 된다. 따라서 고아 객체 제거 기능을 활성화 하면, 부모를 제거할 때 자식도 함께 제거된다. 이것은 `CascadeType.REMOVE`처럼 동작한다.

### 영속성 전이 + 고아 객체, 생명주기

- **`CascadeType.ALL`+ `orphanRemoval=true`**
- 스스로 생명주기를 관리하는 엔티테는 `em.persist()`로 영속화, `em.remove()`로 제거
- 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있음
- 도메인 주도 설계(DDD)의 Aggregate Root 개념을 구현할 때 유용

# 기본값 타입

## JPA의 데이터 타입 분류

- **엔티티 타입**
    - `@Entity`로 정의하는 객체
    - 데이터가 변해도 식별자로 지속해서 추적 가능
    - 예) 회원 엔티티의 키나 나이 값을 변경해도 식별자로 인식 가능
- **값 타입**
    - `int`, `Integer`, `String`처럼 단순 값을 사용하는 자바 기본 타입이나 객체
    - 식별자가 없고 값만 있으므로 변경시 추적 불가
    - 예) 숫자 100을 200으로 변경하면 완전히 다른 값으로 대체