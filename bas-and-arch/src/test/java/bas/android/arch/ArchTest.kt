package bas.android.arch

import bas.android.arch.ArchTest.LocalDataSource.Companion.GUEST
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test

/**
 * Created by Lucio on 2022/1/7.
 */
class ArchTest {

    class Author(val name: String)

    class LocalDataSource {

        suspend fun getData(): Author? {
            delay(1000)
            return cache
        }

        suspend fun modifyData(data: Author) {
            delay(1000)
            Companion.cache = data
        }

        companion object {
            private var cache: Author? = null

            val GUEST = Author("游客")
        }
    }

    class RemoteDataSource {

        companion object {
            var count = 0
        }

        suspend fun fetchData(): Author {
            delay(1000)
            count++
            return Author("Name:$count")
        }
    }

    class Repository(val scope: CoroutineScope) {
        private val remote = RemoteDataSource()

        private val local = LocalDataSource()

        private val _authorObservable = MutableStateFlow<Author>(GUEST)

        val authorObservable = _authorObservable.asStateFlow()

        init {
            scope.launch {
                _authorObservable.update {
                    var data = local.getData()
                    if (data == null) {
                        data = remote.fetchData()
                    }
                    data
                }
//                emit(data)
            }
        }

//        flow<Author> {
//            var data = local.getData()
//            if (data == null) {
//                data = remote.fetchData()
//            }
//            emit(data)
//        }.stateIn(scope)


        suspend fun modifyData(author: Author) {
            local.modifyData(author)
            _authorObservable.compareAndSet(_authorObservable.value,author)
        }
    }


    class Client(val name: String, val repository: Repository) {

        @InternalCoroutinesApi
        suspend fun invoke() {
            val flow = repository.authorObservable
            flow.collect { author ->
                println("Client@${name}:author@${author.hashCode()}=${author.name}")
            }

        }
    }

    @InternalCoroutinesApi
    @Test
    fun test(): Unit = runBlocking {
        val repository = Repository(this)

        val clinet1 = Client("1", repository)

        val client2 = Client("2", repository)

        async {
            clinet1.invoke()
        }

        async {
            delay(100)
            client2.invoke()
        }


        launch {
            delay(3000)
            repository.modifyData(Author("手动更新"))
        }

        delay(5000)

        async {
            Client("22", repository).invoke()
        }.await()

        this.cancel()
    }
}