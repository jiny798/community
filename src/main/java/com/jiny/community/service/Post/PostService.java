package com.jiny.community.service.Post;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.Category;
import com.jiny.community.domain.Post;
import com.jiny.community.dto.Post.PostDetailResponseDto;
import com.jiny.community.dto.Post.PostResponseDto;
import com.jiny.community.dto.Post.PostForm;
import com.jiny.community.repository.CategoryRepository;
import com.jiny.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    //게시글 등록
    @Transactional
    public Long addPost(Account account, PostForm postForm){ //
        Category category = categoryRepository.findByName(postForm.getCategory());

        Post post = Post.createPost(account,postForm.getTitle(),postForm.getContent(),category);
        postRepository.save(post);
        return post.getId();

    }

    //게시글 상세 조회
    public PostDetailResponseDto getDetail(Long postId){

        Post post =  postRepository.findById(postId).get();;
        PostDetailResponseDto postResponseDto = new PostDetailResponseDto();
        postResponseDto.setId(postId);
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setContent(post.getContent());
        postResponseDto.setNickname(post.getAccount().getNickname());
        postResponseDto.setStarCnt(post.getStar());


        return postResponseDto;
    }

    //게시글 수정
    @Transactional
    public void updatePost(Long postId, PostResponseDto postResponseDto){
        Post post =  postRepository.findById(postId).get();
        post.setTitle(postResponseDto.getTitle());
        post.setContent(postResponseDto.getContent());

    }


    public List<PostResponseDto> getPostList(String categoryName){
        Category category = categoryRepository.findByName(categoryName);
        List<Post> post_list = postRepository.findByCategory(category);
        ArrayList<PostResponseDto> posts = new ArrayList<>();

        for(int i =0;i<post_list.size();i++){
//            PostResponseDto postResponseDto = new PostResponseDto();
//            postResponseDto.setTitle(post_list.get(i).getTitle());
//            postResponseDto.setContent(post_list.get(i).getContent());
//            postResponseDto.setNickname(post_list.get(i).getAccount().getNickname());
//            postResponseDto.setId(post_list.get(i).getId());
//            posts.add(postResponseDto);
        }
        return posts;
    }

    public Page<PostResponseDto> getPagingList(Pageable pageable, int pageNo, String categoryName, String name) {

        pageable = PageRequest.of(pageNo, 5 , Sort.by(Sort.Direction.DESC, name));
        Category category=categoryRepository.findByName(categoryName);

        Page<Post> page = postRepository.findByCategory(category,pageable);
        Page<PostResponseDto> postList = page.map(p -> new PostResponseDto(p.getId(),
                                            p.getAccount().getNickname(),
                                            p.getTitle(),
                                            p.getContent(),
                                            p.getCategory().getName(),
                                            p.getStar()));
        //page를 유지하면서 Dto변환
        //    public Long id;
        //    public String nickname;
        //    public String title;
        //    public String content;
        //    public String category;
        //    public Long starCnt;
        return postList;
    }



}
