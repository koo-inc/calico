package factory

import helper.factory.DataFactory

/**
 * Created by tasuku on 15/04/14.
 */
class UserFactory extends DataFactory {{
  define(id: "user", as: "user_info") {n ->
    login_id = "login_id${n}"
    password = "password${n}"
  }
  define(id: "admin", extends: "user") {
    login_id = "admin2"
  }
}}
