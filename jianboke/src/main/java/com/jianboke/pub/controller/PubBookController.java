package com.jianboke.pub.controller;

import com.jianboke.domain.User;
import com.jianboke.enumeration.HttpReturnCode;
import com.jianboke.enumeration.ResourceName;
import com.jianboke.model.RequestResult;
import com.jianboke.model.UsersModel;
import com.jianboke.repository.BookRepository;
import com.jianboke.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by pengxg on 2017/8/6.
 */
@RestController
@RequestMapping(value = "/pub")
public class PubBookController {
    private final Logger log = LoggerFactory.getLogger(PubBookController.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> get(@PathVariable Long id) {
        log.info("rest to request a book :{}", id);
        return Optional.ofNullable(bookRepository.findOne(id))
                .map(book -> ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, book)))
                .orElse(ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_RESOURCE_NOT_FOUND, null)));

    }

    @RequestMapping(value = "/book/queryAllByUsername/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> queryAllByUsername(@PathVariable String username) {
        log.info("REST request to query all books by username:{}", username);
        return userRepository.findOneByUsername(username)
                .map(user -> ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS,
                            bookRepository.findAllByAuthorId(user.getId()))))
                .orElseGet(null);
    }
}
