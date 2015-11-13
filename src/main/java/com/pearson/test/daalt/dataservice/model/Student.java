package com.pearson.test.daalt.dataservice.model;

import java.util.Comparator;

public interface Student extends User, Comparator<Student>, Comparable<Student> {

}
