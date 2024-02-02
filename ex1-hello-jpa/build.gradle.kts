plugins {
  kotlin("jvm") version "1.9.22"
  id("org.jetbrains.kotlin.plugin.jpa") version "1.9.22"
  id("org.jetbrains.kotlin.plugin.allopen") version "1.9.22"
}

noArg {
  annotations("javax.persistence.Entity")
  annotations("javax.persistence.Embeddable")
  annotations("javax.persistence.MappedSuperclass")
}

allOpen {
  annotation("javax.persistence.Entity")
  annotation("javax.persistence.Embeddable")
  annotation("javax.persistence.MappedSuperclass")
}



group = "jpa-basic"
version = "0.0.1"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  
  // hibernate
  implementation("org.hibernate.orm:hibernate-core:6.4.2.Final")
  
  // h2
  implementation("com.h2database:h2:2.2.224")
  
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(17)
}