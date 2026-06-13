# Maven 发布指南

本文档说明如何将 `contactlistview` 库发布到 Maven Central。

## Maven 坐标

| 字段 | 值 |
|------|-----|
| Group ID | `io.github.matkurban` |
| Artifact ID | `contactlistview` |
| Version | 见 `gradle/libs.versions.toml` 中的 `library` |

## 凭证配置

在本地 `gradle.properties` 或 `~/.gradle/gradle.properties` 中配置：

```properties
mavenCentralAutomaticPublishing=false
mavenCentralUsername=<用户名>
mavenCentralPassword=<令牌>
signing.keyId=<GPG_密钥_ID>
signing.password=<GPG_密钥口令>
signing.secretKeyRingFile=<secring.gpg_绝对路径>
```

## 发布命令

```bash
./gradlew :contactlistview:jvmTest
./gradlew :contactlistview:publishToMavenCentral
./gradlew :contactlistview:publishAndReleaseToMavenCentral
```

发布配置位于 [`contactlistview/build.gradle.kts`](contactlistview/build.gradle.kts)。
