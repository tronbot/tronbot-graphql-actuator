Feature: Generate GraphQL Schema
Scenario: Generate GraphQL Schema
  Given request for the jwt token
  Then populate jwt in request header
  Then get request for generate GraphQL schema
