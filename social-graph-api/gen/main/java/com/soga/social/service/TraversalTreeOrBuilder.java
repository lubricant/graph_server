// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: social_graph.proto

package com.soga.social.service;

public interface TraversalTreeOrBuilder extends
    // @@protoc_insertion_point(interface_extends:social.TraversalTree)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 token = 1;</code>
   */
  long getToken();

  /**
   * <code>.social.TraversalNode root = 2;</code>
   */
  boolean hasRoot();
  /**
   * <code>.social.TraversalNode root = 2;</code>
   */
  com.soga.social.service.TraversalNode getRoot();
  /**
   * <code>.social.TraversalNode root = 2;</code>
   */
  com.soga.social.service.TraversalNodeOrBuilder getRootOrBuilder();
}
