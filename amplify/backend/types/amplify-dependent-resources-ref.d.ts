export type AmplifyDependentResourcesAttributes = {
  "analytics": {
    "taskmaster": {
      "Id": "string",
      "Region": "string",
      "appName": "string"
    }
  },
  "api": {
    "taskmaster": {
      "GraphQLAPIEndpointOutput": "string",
      "GraphQLAPIIdOutput": "string",
      "GraphQLAPIKeyOutput": "string"
    }
  },
  "auth": {
    "taskmaster70ae126b": {
      "AppClientID": "string",
      "AppClientIDWeb": "string",
      "IdentityPoolId": "string",
      "IdentityPoolName": "string",
      "UserPoolArn": "string",
      "UserPoolId": "string",
      "UserPoolName": "string"
    }
  },
  "predictions": {
    "speechGenerator3bc8e8f0": {
      "language": "string",
      "region": "string",
      "voice": "string"
    },
    "translateText374ed8e7": {
      "region": "string",
      "sourceLang": "string",
      "targetLang": "string"
    }
  },
  "storage": {
    "taskmaster": {
      "BucketName": "string",
      "Region": "string"
    }
  }
}