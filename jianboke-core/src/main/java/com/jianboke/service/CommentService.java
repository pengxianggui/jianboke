package com.jianboke.service;

import com.jianboke.domain.Comment;
import com.jianboke.repository.CommentLikeRepository;
import com.jianboke.repository.CommentRepository;
import com.jianboke.repository.ReplyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by pengxg on 2017/8/25.
 */
@Service("CommentService")
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Transactional
    public void deleteComment(Comment comment) {
        commentLikeRepository.deleteAllByCommentId(comment.getId());
        replyRepository.deleteAllByCommentId(comment.getId());
        commentRepository.delete(comment);
    }
}
