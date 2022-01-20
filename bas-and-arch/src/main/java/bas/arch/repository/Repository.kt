package bas.arch.repository

import bas.arch.repository.datasource.ExampleLocalDataSource
import bas.arch.repository.datasource.ExampleRemoteDataSource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Lucio on 2022/1/7.
 * 数据层架构（DataLayer-官方文档链接：https://developer.android.google.cn/jetpack/guide/data-layer?hl=zh_cn）
 *
 * Repository（存储库类） -> DataSource（数据源类）
 *
 *
 * 存储库类（Repository）负责以下任务：
 *      1 向应用的其余部分公开数据。
 *      2 集中处理数据变化。
 *      3 解决多个数据源之间的冲突。
 *      4 对应用其余部分的数据源进行抽象化处理。
 *      5 包含业务逻辑。
 *
 * Repository命名惯例：数据类型 + Repository
 *      例如：NewsRepository、MoviesRepository 或 PaymentsRepository
 *
 * DataSource命名惯例：数据类型 + 来源类型 + DataSource。
 *      对于数据的类型，可以使用 Remote 或 Local，以使其更加通用，因为实现是可以变化的。例如：NewsRemoteDataSource 或 NewsLocalDataSource。
 *      在来源非常重要的情况下，为了更加具体，可以使用来源的类型。例如：NewsNetworkDataSource 或 NewsDiskDataSource。
 *      请勿根据实现细节来为数据源命名（例如 UserSharedPreferencesDataSource），因为使用相应数据源的存储库应该不知道数据是如何保存的。如果您遵循此规则，便可以更改数据源的实现（例如，从 SharedPreferences 迁移到 DataStore），而不会影响调用相应数据源的层。
 *      注意：迁移到数据源的新实现时，您可以为数据源创建接口，并使用两种数据源实现：一种用于旧的后备技术，另一种用于新的技术。在这种情况下，您可以将技术名称用作数据源类名称（尽管它是一个实现细节），因为存储库只能看到接口，而看不到数据源类本身。完成迁移后，您可以重命名新类，使其名称中不包含实现细节。
 *
 * Repository（存储库类）：
 *
 *  数据层由多个存储库组成，其中每个存储库都可以包含零到多个数据源。
 *  该层公开的数据应该是不可变的，这样就可以避免数据被其他类篡改，从而避免数值不一致的风险。
 *  注意：通常，如果存储库只包含单个数据源并且不依赖于其他存储库，开发者会将存储库和数据源的职责合并到存储库类中。这种情况下，在应用的更高版本中，如果存储库需要处理来自其他来源的数据，请不要忘记拆分这些功能。
 *
 *  按照依赖项注入方面的最佳实践，存储库应在其构造函数中将数据源作为依赖项
 *
 *  多层存储库：在某些涉及更复杂业务要求的情况下，存储库可能需要依赖于其他存储库。这可能是因为所涉及的数据是来自多个数据源的数据聚合，或者是因为相应职责需要封装在其他存储库类中。
 *      例如，负责处理用户身份验证数据的存储库 UserRepository 可以依赖于其他存储库（例如 LoginRepository 和 RegistrationRepository），以满足其要求。
 *  注意：传统上，一些开发者将依赖于其他存储库类的存储库类称为 manager，例如称为 UserManager 而非 UserRepository。如果您愿意，可以使用此命名惯例。
 *
 * DataSource（数据源类）：
 *
 *  每个数据源类应仅负责处理一个数据源，相应数据源可以是文件、网络来源或本地数据库。
 *
 *  层次结构中的其他层绝不能直接访问数据源；数据层的入口点始终是存储库类。
 *  状态容器类（请参阅界面层指南）或用例类（请参阅网域层指南）绝不能将数据源作为直接依赖项。
 *
 *
 * 数据层中的类通常会公开函数，以执行一次性的创建、读取、更新和删除 (CRUD) 调用，或接收关于数据随时间变化的通知。对于每种情况，数据层都应公开以下内容：
 *      1、一次性操作：在 Kotlin 中，数据层应公开挂起函数；对于 Java 编程语言，数据层应公开用于提供回调来通知操作结果的函数，或公开 RxJava Single、Maybe 或 Completable 类型。
 *      2、接收关于数据随时间变化的通知：在 Kotlin 中，数据层应公开数据流；对于 Java 编程语言，数据层应公开用于发出新数据的回调，或公开 RxJava Observable 或 Flowable 类型。
 *
 *
 * 每个存储库都只定义单个可信来源，这一点非常重要，可信来源始终包含一致、正确且最新的数据。实际上，从存储库公开的数据应始终是直接来自可信来源的数据。建议使用本地数据源（例如数据库）作为可信来源。
 *
 * 这个含义非常重要，可能无法理解，举个例子：比如有一个UserRepository包含了Remote和Local两个数据源，从网络或者从本地获取用户信息对外提供，由于有两个数据源都涉及了用户信息User，
 * 那么我们应该定义某一个数据源为可信来源，也就意味对外公开的User数据应该来源于可信来源，而建议我们使用本地数据作为可信来源。
 * 为什么使用本地，举个例子，假如我们使用网络数据作为可信来源，那么对外暴露的数据源是从网络获取的，网络获取之后缓存到本地，但是存在一个情况，网络获取数据的数据成功之后，应该先将其缓存到本地，再对外通知，
 * 不然先对外通知之后，缓存到本地的操作失败了，就导致了网络数据源于本地数据源可能存在差异（假如还存在其他存储库类使用了以本地数据源为可信来源的操作，就可能导致业务的不一致），由于是缓存到本地成功之后再对外通知，不等同于用本地数据源作为可信来源么？这是个人理解。
 *
 *
 * 调用数据源和存储库应该具有主线程安全性（即从主线程调用是安全的）。
 */

class ExampleRepository(
    private val exampleRemoteDataSource: ExampleRemoteDataSource, // network
    private val exampleLocalDataSource: ExampleLocalDataSource // database
) {

//    val data: Flow<ExampleData> = ...
//
//    suspend fun modifyData(example: Example) { ... }
}

class ExampleData
