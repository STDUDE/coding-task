package com.test.codingtask.web;

import org.springframework.data.jpa.repository.JpaRepository;

interface DataItemRepository extends JpaRepository<DataItem, String> {

}