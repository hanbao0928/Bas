package bas.arch.usecase

/**
 * Created by Lucio on 2022/1/7.
 * Domain 层
 *
 * 命名惯例 一般现在时动词 + 名词/内容（可选）+ 用例。
 *  例如：FormatDateUseCase、LogOutUserUseCase、GetLatestNewsWithAuthorsUseCase 或 MakeLoginRequestUseCase。
 */

/*
* 注意：在某些情况下，用例中可能存在的逻辑可以成为 Util 类中静态方法的一部分。
* 不过，不建议采用后者，因为 Util 类通常很难找到，而且其功能也很难发现。
* 此外，用例还可以共享通用功能（例如基类中的线程处理和错误处理），这对规模较大的大型团队很有助益。
* */
