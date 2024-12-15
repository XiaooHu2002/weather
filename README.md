# weather

#### 介绍

天气预报APP，可以显示中国城市的天气数据，具有城市收藏功能，可以查看未来2天的预报数据。 在首页的头部，点击地区可以进入收藏夹。默认显示北京的天气。
支持最低安卓系统5.0，已适配目前最新安卓系统13。

#### 注意事项

1. 和风天气服务，因为是免费版，使用额度有上限，具体是每天最多调用1000次，第二天重新计算
2. 和风天气服务，因为是免费版，未来天气只能显示未来2天
3. 编程软件是androidStudio，版本是Android Studio Giraffe | 2022.3.1 Patch 3
4. 以androidX为基础库开发
5. 未使用数据库存储，而是用SharedPreferences来存储轻量化数据
6. 中国城市列表信息是只读取不修改的，所以以文件的形式存放，位置是在app/src/main/assets/China-City-List.txt，数据结构是json格式

#### 第三方库

1. 和风天气服务（https://dev.qweather.com）
2. 网络请求（okhttp3）
3. json解析器（gson）