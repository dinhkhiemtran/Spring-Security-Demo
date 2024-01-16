package com.example.springsecuritydemo.security;

public interface Sanitizer<T> {
  T sanitizer(T t);
}
