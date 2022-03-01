package bas.lang.annotation

/**
 * 备注
 */
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
annotation class Note(val message:String)