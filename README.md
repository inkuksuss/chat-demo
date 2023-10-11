1. JPA 트랜잭션 매니져는 JDBC도 함께 동작할 수 있음
2. 한 트랜잭션 내 redis가 포함되면 TransactionSynchronizationManager의 synchronizations의 redisConnection을 가지고 있음.
    - 롤백 시 AbstractPlatformTransactionManager에서 모든 synchronizations invokeAfterCompletion() 호출 -> redis discard 
3. 트랜잭션에 redis의 커넥션을 가저오면 (RedisConnectionUtils - doGetConnection()) synchronizations에 RedisTransactionSynchronizer를 set함
      