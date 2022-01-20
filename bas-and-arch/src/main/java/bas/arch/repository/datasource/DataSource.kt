package bas.arch.repository.datasource

/**
 * Created by Lucio on 2022/1/7.
 * 数据层架构 - 数据源
 *
 *
 *
 *
 */



class ExampleRemoteDataSource(){

}

class ExampleLocalDataSource(){

}

/**
 * 远程数据源
 */
class RemoteDataSource{

}

/**
 * 本地数据源：比如可以包括内存、数据库等数据源
 */
class LocalDataSource(val databaseSource: DatabaseDataSource,val memorySource:MemoryDataSource){

}

/**
 * 数据库数据源
 */
class DatabaseDataSource{

}

/**
 * 内存数据源
 */
class MemoryDataSource{

}
