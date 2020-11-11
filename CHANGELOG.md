# Changelog

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Changed
- [#183](https://github.com/javaswift/joss/pull/183): Bump jackson from 1.x to 2.x, target JDK from 1.6 to 1.8 [@didot](https://github.com/didot) [#175](https://github.com/javaswift/joss/issues/175()

## [0.10.3](https://github.com/javaswift/joss/releases/tag/v0.10.3) - 2019-05-20
### Changed
- [#156](https://github.com/javaswift/joss/pull/156): Adds support for KeystoneV3 scoping during auth [@tfelix](https://github.com/tfelix) [#155](https://github.com/javaswift/joss/issues/155)  

## [0.10.2](https://github.com/javaswift/joss/releases/tag/v0.10.2) - 2018-4-5
### Changed
- [#148](https://github.com/javaswift/joss/issues/148): Prevent HTTP connection pool exhaustion when an exception occurs by [@wangbinhui](https://github.com/wangbinhui) [#128](https://github.com/javaswift/joss/pull/128)  

## [0.10.1](https://github.com/javaswift/joss/releases/tag/v10.0.1) - 2018-2-14
### Added
- [#63](https://github.com/javaswift/joss/issues/63): Added segmentation support for uploading objects via InputStream by [@infinitydev](https://github.com/infinitydev) and [@arunazaraiah](https://github.com/arunazaraiah) [#74](https://github.com/javaswift/joss/pull/74)

## [0.10.0](https://github.com/javaswift/joss/releases/tag/v0.10.0) - 2018-1-24
### Added
- [#133](https://github.com/javaswift/joss/issues/133): Added support for [Keystone v3.0 authentication](https://developer.openstack.org/api-ref/identity/v3/index.html#authentication-and-token-management) by [@kevinconaway](https://github.com/kevinconaway) [#142](https://github.com/javaswift/joss/pull/142)
### Changed
- [#102](https://github.com/javaswift/joss/issues/102): Fixed bug which caused tempUrl to generate hash signature from relative path instead of absolute path by [@thomasritscher](https://github.com/thomasritscher) [#146](https://github.com/javaswift/joss/pull/146)  
- [#104](https://github.com/javaswift/joss/issues/104): Fixed bug which did not allow tenant name and id to be empty strings by [@LiamJolly](https://github.com/LiamJolly) [#105](https://github.com/javaswift/joss/pull/105)  

## [0.9.17](https://github.com/javaswift/joss/releases/tag/v0.9.17) - 2017-10-25
### Added
- [#85](https://github.com/javaswift/joss/issues/85): Configurable proxy support by [@lebe-dev](https://github.com/lebe-dev) [#132](https://github.com/javaswift/joss/pull/132) (refactoring by [@robert-bor](https://github.com/robert-bor) [#141](https://github.com/javaswift/joss/pull/141))

### Changed
- [#72](https://github.com/javaswift/joss/issues/72): Fixed bug which caused account reauthentication to forget preferred region by [@jbolla](https://github.com/jbolla) [#115](https://github.com/javaswift/joss/pull/115)
- [#137](https://github.com/javaswift/joss/issues/137): Fixed bug which caused an exception when trying to list containers in a swift version higher than 2.13.0 by [@csommelet](https://github.com/csommelet) [#140](https://github.com/javaswift/joss/pull/140)

## [0.9.16](https://github.com/javaswift/joss/releases/tag/v0.9.16) - 2017-08-31

### Changed
- [#107](https://github.com/javaswift/joss/issues/72): Fixed bug which caused **already existing** containers permissions (makePublic, makePrivate) to be unchangeable through JOSS by [@zhangsw](https://github.com/zhangsw) [#108](https://github.com/javaswift/joss/pull/108)

## [0.9.15](https://github.com/javaswift/joss/releases/tag/v0.9.15) - 2017-06-15

### Changed
- [#130](https://github.com/javaswift/joss/pull/130) Fixed bug which caused timezone to reset to user's local timezone in specific cases by [@djalova](https://github.com/djalova)
