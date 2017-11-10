package jsonparser.test.joe;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by joe321 on 2016/12/9.
 */
@Data
public class Resource {

    String type;

    String Method;

    String path;

    //optional
    String Comment;

    //optional
    //ResourceInput [] inputs;

    //optional
//    ResourceOutput [] outputs;

    //optional
  //  ResourceAuth auth;

    //default OK
    String expected;

    //optional
    String [] alternatives;

    //optional
  //  Map<String, ExceptionDef> exceptions;

    //optional
    boolean async;

    //optional
//    Map<ExtendedAnnotation, String> annotations;

    //optional
    String [] consumes;

    //optional
    String [] produces;

    //optional
//    Identifier name;

}
