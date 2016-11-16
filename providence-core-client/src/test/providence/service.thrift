namespace java net.morimekta.test.providence.srv


struct Request {
    1: string text;
}

struct Response {
    1: string text;
}

exception Failure {
    1: string text;
}

service BaseService {
    oneway void ping();
}

service MyService extends BaseService {
    Response test(1: Request request) throws (1: Failure f);

    double test2(2: i64 a, 4: byte late);
}

service MyService2 {
    Response testing(1: Request request) throws (1: Failure f);

    double test2(2: i64 a, 4: byte late);
}
