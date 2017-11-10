```
{
    "namespace": "com.yahoo.ecosystem.mobile_payment.parsec",
    "name": "Transaction",
    "version": 1,
    "types": [
        {
            "StructTypeDef": {
                "type": "Struct",
                "name": "ParsecErrorDetail",
                "comment": "Parsec error detail",
                "fields": [
                    {
                        "name": "message",
                        "type": "String"
                    },
                    {
                        "name": "invalidValue",
                        "type": "String"
                    }
                ]
            }
        },
        {
            "StructTypeDef": {
                "type": "Struct",
                "name": "ParsecErrorBody",
                "comment": "Parsec error response entity object",
                "fields": [
                    {
                        "name": "code",
                        "type": "Int32",
                        "comment": "error code"
                    },
                    {
                        "name": "message",
                        "type": "String",
                        "comment": "error message"
                    },
                    {
                        "name": "detail",
                        "type": "Array",
                        "comment": "error detail",
                        "items": "ParsecErrorDetail"
                    }
                ]
            }
        },
        {
            "StructTypeDef": {
                "type": "Struct",
                "name": "ParsecResourceErrorEE",
                "comment": "This error model is designed for following EC REST API Convention (yo/ecrest)",
                "fields": [
                    {
                        "name": "error",
                        "type": "ParsecErrorBody",
                        "comment": "error object"
                    }
                ]
            }
        },
        {
            "StructTypeDef": {
                "type": "Struct",
                "name": "MpTransaction",
                "fields": [
                    {
                        "name": "currency",
                        "type": "String",
                        "annotations": {
                            "x_currency": "",
                            "x_not_null": ""
                        }
                    }
                ]
            }
        }
    ],
    "resources": [
        {
            "type": "MpTransaction",
            "method": "GET",
            "path": "/transactions/{transactionId}",
            "inputs": [
                {
                    "name": "transactionId",
                    "type": "String",
                    "comment": "The transaction id",
                    "pathParam": true
                }
            ],
            "auth": {
                "authenticate": true
            },
            "expected": "OK",
            "exceptions": {
                "BAD_REQUEST": {
                    "type": "ParsecResourceErrorEE"
                },
                "FORBIDDEN": {
                    "type": "ParsecResourceErrorEE"
                },
                "INTERNAL_SERVER_ERROR": {
                    "type": "ParsecResourceErrorEE"
                },
                "NOT_FOUND": {
                    "type": "ParsecResourceErrorEE"
                },
                "UNAUTHORIZED": {
                    "type": "ParsecResourceErrorEE"
                }
            },
            "name": "getTransaction"
        }
    ]
}
```