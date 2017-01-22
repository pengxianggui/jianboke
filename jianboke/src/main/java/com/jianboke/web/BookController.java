package com.jianboke.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jianboke.domain.Book;
import com.jianboke.domain.User;
import com.jianboke.repository.BookRepository;
import com.jianboke.service.UserService;
import com.jianboke.utils.FileUploadUtils;

@RestController
@RequestMapping("/api")
public class BookController {
	private static final Logger log = LoggerFactory.getLogger(BookController.class);
	
	@Autowired
	private FileUploadUtils fileUploadUtils;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 保存Book封面的api，返回存储路径字符串
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/book/cover", method = RequestMethod.POST)
	public Map<String, String> ulpoadCover(@RequestParam("file") MultipartFile file) {
		return fileUploadUtils.uploadBookCover(file);
	}
	
	/**
	 * 保存Book
	 * @param book
	 * @return
	 */
	@RequestMapping(value = "/book", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Book createBook(@Valid @RequestBody Book book) {
		User user = userService.getUserWithAuthorities();
		book.setAuthorId(user.getId());
		Book b = bookRepository.save(book);
		log.debug("REST request to save Book: {}", book);
		return b;
	}
	
	/**
	 * 用户查询所有自己的books
	 * @return
	 */
	@RequestMapping(value = "/book", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Book> query() {
		User user = userService.getUserWithAuthorities();
		List<Book> bookList = bookRepository.findAllByAuthorId(user.getId());
		log.debug("REST request to get All Books by authoId : {}", user.getId());
		return bookList;
	}
	
	/**
	 * 删除一本书。是否存在把别人的book删除的系统漏洞？
	 * @param id
	 */
	@RequestMapping(value = "/book/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void delete(@PathVariable Long id) {
		Book book = bookRepository.findOne(id);
		bookRepository.delete(book);
		log.debug("REST request to delete a book : {}", book);
	}
	
	/**
	 * update a book
	 * @param book
	 * @return
	 */
	public Book update(@Valid @RequestBody Book book) {
		log.debug("REST request to update a book : {}", book);
		if (bookRepository.getOne(book.getId()) != null) {
			return bookRepository.save(book);
		}
		return null;
	}
}
