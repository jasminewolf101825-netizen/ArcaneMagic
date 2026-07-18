# ArcaneMagic

Minecraft Java 26.2 Fabric 傳送核心測試專案。

## 第一版功能

- 新增「傳送核心」
- 用核心對原版磁石右鍵進行綁定
- 朝空氣右鍵核心傳送回磁石
- 三秒冷卻
- 目前限同一維度
- 離開世界後綁定資料會重設

## 自動編譯

每次上傳或修改檔案後，GitHub Actions 會使用 Java 25 與 Gradle 9.5.1 執行建置，並將 `build/libs/` 上傳為 Artifact。
