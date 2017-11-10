# This is auto generated tmp swagger page
```
{
    "swagger": "2.0",
    "info": {
        "title": "The Cobrand API",
        "version": "1"
    },
    "basePath": "/api/cobrand/v1",
    "schemes": [],
    "paths": {
        "/yca/cards": {
            "get": {
                "tags": [
                    "CoCardCollection"
                ],
                "produces": [
                    "application/json"
                ],
                "parameters": [
                    {
                        "name": "guid",
                        "in": "query",
                        "type": "string",
                        "required": true
                    }
                ],
                "responses": {
                    "200": {
                        "description": "OK",
                        "schema": {
                            "$ref": "#/definitions/CoCardCollection"
                        }
                    },
                    "400": {
                        "description": "Bad Request - The user given wrong format of guid or wrong format nid.",
                        "schema": {
                            "$ref": "#/definitions/ResourceError"
                        }
                    },
                    "401": {
                        "description": "Unauthorized",
                        "schema": {
                            "$ref": "#/definitions/ResourceError"
                        }
                    },
                    "403": {
                        "description": "Forbidden",
                        "schema": {
                            "$ref": "#/definitions/ResourceError"
                        }
                    },
                    "500": {
                        "description": "Internal Server Error - The system have error.",
                        "schema": {
                            "$ref": "#/definitions/ResourceError"
                        }
                    }
                }
            }
        }
    },
    "definitions": {
        "CoCard": {
            "properties": {
                "cardNumber": {
                    "type": "string",
                    "example": ""
                },
                "guid": {
                    "type": "string",
                    "example": ""
                },
                "id": {
                    "type": "string",
                    "example": ""
                },
                "modifiedTime": {
                    "type": "string",
                    "example": ""
                },
                "status": {
                    "$ref": "#/definitions/CoCardStatus"
                }
            },
            "required": [
                "cardNumber",
                "modifiedTime",
                "status",
                "id",
                "guid"
            ]
        },
        "CoCardCollection": {},
        "CoCardStatus": {},
        "ParsecErrorBody": {
            "properties": {
                "code": {
                    "type": "integer",
                    "format": "int32"
                },
                "detail": {
                    "type": "array",
                    "items": {
                        "$ref": "#/definitions/ParsecErrorDetail"
                    }
                },
                "message": {
                    "type": "string"
                }
            },
            "required": [
                "message"
            ]
        },
        "ParsecErrorDetail": {
            "properties": {
                "invalidValue": {
                    "type": "string"
                },
                "message": {
                    "type": "string"
                }
            },
            "required": [
                "message"
            ]
        },
        "ParsecResourceError": {
            "properties": {
                "error": {
                    "$ref": "#/definitions/ParsecErrorBody"
                }
            },
            "required": [
                "error"
            ]
        },
        "ResourceError": {
            "properties": {
                "code": {
                    "type": "integer",
                    "format": "int32"
                },
                "message": {
                    "type": "string"
                }
            },
            "required": [
                "code",
                "message"
            ]
        }
    }
}
```
```
{
    "swagger": "2.0",
    "info": {
        "title": "The sample API",
        "version": "1"
    },
    "basePath": "/api/sample/v1",
    "schemes": [],
    "paths": {
        "/empty": {
            "get": {
                "tags": [
                    "MpNullResult"
                ],
                "produces": [
                    "application/json"
                ],
                "responses": {
                    "200": {
                        "description": "OK",
                        "schema": {
                            "$ref": "#/definitions/MpNullResult"
                        }
                    },
                    "400": {
                        "description": "Bad Request",
                        "schema": {
                            "$ref": "#/definitions/ResourceError"
                        }
                    },
                    "401": {
                        "description": "Unauthorized",
                        "schema": {
                            "$ref": "#/definitions/ResourceError"
                        }
                    },
                    "403": {
                        "description": "Forbidden",
                        "schema": {
                            "$ref": "#/definitions/ResourceError"
                        }
                    },
                    "500": {
                        "description": "Internal Server Error",
                        "schema": {
                            "$ref": "#/definitions/ResourceError"
                        }
                    }
                }
            }
        }
    },
    "definitions": {
        "MpNullResult": {
            "description": "The object is just for no content"
        },
        "ParsecErrorBody": {
            "properties": {
                "code": {
                    "type": "integer",
                    "format": "int32"
                },
                "detail": {
                    "type": "array",
                    "items": {
                        "$ref": "#/definitions/ParsecErrorDetail"
                    }
                },
                "message": {
                    "type": "string"
                }
            },
            "required": [
                "message"
            ]
        },
        "ParsecErrorDetail": {
            "properties": {
                "invalidValue": {
                    "type": "string"
                },
                "message": {
                    "type": "string"
                }
            },
            "required": [
                "message"
            ]
        },
        "ParsecResourceError": {
            "properties": {
                "error": {
                    "$ref": "#/definitions/ParsecErrorBody"
                }
            },
            "required": [
                "error"
            ]
        },
        "ResourceError": {
            "properties": {
                "code": {
                    "type": "integer",
                    "format": "int32"
                },
                "message": {
                    "type": "string"
                }
            },
            "required": [
                "code",
                "message"
            ]
        }
    }
}
```
Wed Mar 1 17:05:50 CST 2017
