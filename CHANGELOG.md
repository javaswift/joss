# Changelog

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- [#133](https://github.com/javaswift/joss/issues/133): Added support for [Keystone v3.0 authentication](https://developer.openstack.org/api-ref/identity/v3/index.html#authentication-and-token-management) by @kevinconaway [#142](https://github.com/javaswift/joss/pull/142)
### Changed
- [#102](https://github.com/javaswift/joss/issues/102): Fixed bug which caused tempUrl to generate hash signature from relative path instead of absolute path by @thomasritscher [#146](https://github.com/javaswift/joss/pull/146)  
- [#104](https://github.com/javaswift/joss/issues/104): Fixed bug which did not allow tenant name and id to be empty strings by @LiamJolly [#105](https://github.com/javaswift/joss/pull/105)  

## [0.9.17](https://github.com/javaswift/joss/releases/tag/v0.9.17) - 2017-10-25
### Added
- [#85](https://github.com/javaswift/joss/issues/85): Configurable proxy support by @lebe-dev [#132](https://github.com/javaswift/joss/pull/132) (refactoring by @robert-bor [#141](https://github.com/javaswift/joss/pull/141))

### Changed
- [#72](https://github.com/javaswift/joss/issues/72): Fixed bug which caused account reauthentication to forget preferred region by @jbolla [#115](https://github.com/javaswift/joss/pull/115)
- [#137](https://github.com/javaswift/joss/issues/137): Fixed bug which caused an exception when trying to list containers in a swift version higher than 2.13.0 by @csommelet [#140](https://github.com/javaswift/joss/pull/140)

## [0.9.16](https://github.com/javaswift/joss/releases/tag/v0.9.16) - 2017-08-31

### Changed
- [#107](https://github.com/javaswift/joss/issues/72): Fixed bug which caused **already existing** containers permissions (makePublic, makePrivate) to be unchangeable through JOSS by @zhangsw [#108](https://github.com/javaswift/joss/pull/108)

## [0.9.15](https://github.com/javaswift/joss/releases/tag/v0.9.15) - 2017-06-15

### Changed
- [#130](https://github.com/javaswift/joss/pull/130) Fixed bug which caused timezone to reset to user's local timezone in specific cases by @djalova
