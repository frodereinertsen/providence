namespace java net.morimekta.test.providence


struct Request {
    1: string text;
}

struct Response {
    1: string text;
}

exception Failure {
    1: string text;
}

service MyService {
    oneway void ping();

    Response test(1: Request request) throws (1: Failure f);
}