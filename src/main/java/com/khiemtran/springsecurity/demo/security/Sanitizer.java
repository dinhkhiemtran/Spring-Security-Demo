package com.khiemtran.springsecurity.demo.security;

public interface Sanitizer<T> {
  T sanitize(T t);
}
