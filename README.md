# 口琴谱大全（HarmonicaScore）

Android 原生 Kotlin + Jetpack Compose 口琴谱阅读原型。

## 当前功能
- 歌名、歌手、关键词搜索（本地示例数据）
- 模糊匹配与精准匹配模式
- 示例曲谱阅读与收藏
- 十孔布鲁斯、半音阶、复音口琴类型切换入口
- GitHub Actions 自动构建 Debug APK

## 当前限制
这是可扩展原型，内置少量示例曲谱，尚未连接在线曲谱数据库。完整歌曲库需要使用公有领域、获得授权或用户自行导入的曲谱。不同口琴的吹吸孔位映射与移调转换尚未实现。

## 构建 APK
将项目上传到 GitHub 后，打开 **Actions → Android APK Build**。工作流成功后，下载名为 `harmonica-score-debug-apk` 的 artifact ZIP，解压后即可找到 APK。该 artifact 默认保留 30 天。
