# android-mvvm-architecture

Kotlin 版本的 MVVM 开发框架，适用于中大型项目。

随着 Android 项目的演进，MVVM 已经成为当前比较主流的架构方式之一。这个仓库基于 Kotlin 语言，尝试封装一套适合中大型项目使用的 MVVM 基础框架，重点关注页面、数据、状态管理的分层设计，减少重复代码，提高项目的可维护性和扩展性。

> 这个项目并不是为了替代成熟的主流架构框架，而是在理解 MVVM 思想的基础上，做一套适合自己项目和团队使用的基础实现。

## 项目简介

MVVM 的核心思想是把页面展示、数据处理和状态管理拆开，避免把大量逻辑耦合在 Activity / Fragment 中。

这种架构的优势包括：

- UI 层更轻，页面只负责展示和事件处理
- 业务逻辑更集中，便于维护和扩展
- 数据层和页面层解耦，更容易测试和重构
- 便于在大型项目中统一管理状态和生命周期

---

## MVVM 架构思路

MVVM 的关键组成部分如下：

- View：负责页面展示、用户事件处理
- ViewModel：连接 UI 和数据，负责状态管理
- Repository：负责数据获取、处理和缓存
- LiveData：负责响应式状态更新
- BaseActivity / BaseFragment：统一页面基类，减少重复代码

整体流程可以理解为：

View 层收到用户操作 -> ViewModel 处理业务 -> Repository 获取/处理数据 -> LiveData 通知 UI 更新

---

## 关键模块说明

### 1. BaseActivity

`BaseActivity` 是页面基类，负责封装一些通用能力，例如：

- DataBinding 绑定
- 权限申请
- 返回键处理
- Activity 跳转
- Snackbar 提示
- 退出应用逻辑
- 通用初始化流程

这样每个页面不需要重复写这些通用逻辑，代码会更加清晰。

### 2. BaseFragment

`BaseFragment` 是 Fragment 的通用基类，主要负责：

- 统一初始化流程
- 绑定生命周期
- 处理通用事件
- 减少 Fragment 中重复的模板代码

### 3. BaseViewModel

`BaseViewModel` 是 ViewModel 的基础抽象类，通常用于统一处理：

- 页面状态管理
- LiveData / StateFlow 类型状态
- 封装通用数据回调
- 统一处理业务层和 UI 层之间的通信

### 4. BaseRepository

`BaseRepository` 是数据仓库层的基类，适合处理：

- 网络请求
- 本地缓存
- 数据转换
- 统一数据入口
- 业务逻辑的下沉

这样可以避免 Activity / Fragment 直接负责数据处理。

---

## 推荐使用方式

该框架的典型使用方式是：

1. Activity / Fragment 继承基类
2. 页面通过 ViewModel 管理状态
3. ViewModel 调用 Repository 获取数据
4. UI 通过 LiveData 观察数据更新
5. 页面只做展示和事件回调

示例：

```kotlin
class MainViewModel : BaseViewModel() {

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    fun loadUserInfo() {
        // 通过 Repository 获取数据
        // 结果回调给 UI
    }
}
```

```kotlin
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var viewModel: MainViewModel

    override fun inflateBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.userInfo.observe(this) { userInfo ->
            // 更新 UI
        }
    }
}
```

---

## 项目结构

```text
android-mvvm-architecture/
├── app/
│   └── ...               // 示例工程
├── framework_mvvm/
│   └── ...               // MVVM 基础框架
├── module_utils/
│   └── ...               // 通用工具模块
├── VersionPlugin/
│   └── ...               // 版本管理插件
├── build.gradle
├── settings.gradle
├── gradlew
├── README.md
├── gradle.properties
├── libs.versions.toml
├── screenMatch.properties
├── screenMatch_example_dimens.xml
└── ...
```

其中：

- `app`：演示层和示例页面
- `framework_mvvm`：MVVM 架构核心
- `module_utils`：通用工具模块
- `VersionPlugin`：统一依赖版本管理

---

## 适用场景

这个框架比较适合以下场景：

- 中大型 Android 项目
- 业务逻辑较多，页面较多的项目
- 需要统一数据流和状态管理的项目
- 想让页面代码更清晰、更容易维护
- 需要渐进式接入 MVVM 架构的项目

---

## 优势总结

- 适用于中大型项目
- 结构分层清晰
- 页面职责明确
- 数据流更统一
- 代码复用能力更强
- 更符合现代 Android 架构开发思路

---

## 注意事项

这个项目偏“基础架构封装”，并不是一个大而全的完整业务框架，重点在于：

- 统一页面基类
- 封装 ViewModel 逻辑
- 抽离数据访问层
- 提高项目整体结构清晰度

如果你的项目需要进一步扩展，建议在这个基础上再补充：

- 网络层封装
- 统一错误处理
- 资源管理
- 页面状态管理
- 导航管理
- 数据缓存

---

## 结语

如果这个项目对你有帮助，欢迎在 GitHub 上给它一个 Star。

这类架构型项目的价值，不是“展示炫技”，而是让代码更清晰、更稳定、更容易扩展。MVVM 的意义就在于把复杂的页面逻辑和数据处理拆分开，降低耦合，提高长期维护效率。

Thanks you so much ... END
