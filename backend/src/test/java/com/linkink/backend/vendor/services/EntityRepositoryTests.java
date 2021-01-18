package com.linkink.backend.vendor.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import com.linkink.backend.data.entity.Image;
import com.linkink.backend.data.entity.Post;
import com.linkink.backend.data.entity.Vendor;
import com.linkink.backend.data.repository.ImageRepository;
import com.linkink.backend.data.repository.PostRepository;
import com.linkink.backend.data.repository.VendorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EntityRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private VendorRepository vendorRepository;

    @Test
    public void testJPAtest(){
        Vendor vendor_A= new Vendor("abc", "ABC", "company", "whatever@com", "AB Street", "Tenafly", "USA","NJ","999-999-9999", null);
        Vendor vendor_B= new Vendor("cdf", "CDF", "company", "whatever@com", "AB Street",  "Tenafly", "USA","NJ","999-999-9999", null);
        Vendor vendor_C= new Vendor("efg", "EFG", "company", "whatever@com", "AB Street",  "Tenafly", "USA","NJ","999-999-9999", null);
        entityManager.persist(vendor_A);
        entityManager.persist(vendor_B);
        entityManager.persist(vendor_C);


        Post postA_A=new Post(vendor_A);
        Post postB_A=new Post(vendor_A);
        Post postC_A=new Post(vendor_A);
        entityManager.persist(postA_A);
        entityManager.persist(postB_A);
        entityManager.persist(postC_A);
        vendor_A.getPosts().add(postA_A);
        vendor_A.getPosts().add(postB_A);
        vendor_A.getPosts().add(postC_A);

        Post postA_B=new Post(vendor_B);
        Post postB_B=new Post(vendor_B);
        entityManager.persist(postA_B);
        entityManager.persist(postB_B);
        vendor_B.getPosts().add(postA_B);
        vendor_B.getPosts().add(postB_B);

        Post postA_C=new Post(vendor_C);
        entityManager.persist(postA_C);
        vendor_C.getPosts().add(postA_C);


        Image imageA_A_A= new Image(vendor_A, postA_A,null);
        Image imageB_A_A= new Image(vendor_A, postA_A,null);
        Image imageA_B_A= new Image(vendor_A, postB_A,null);
        entityManager.persist(imageA_A_A);
        entityManager.persist(imageB_A_A);
        entityManager.persist(imageA_B_A);
        postA_A.getImages().add(imageA_A_A);
        postA_A.getImages().add(imageB_A_A);
        postB_A.getImages().add(imageA_B_A);
        vendor_A.getImages().add(imageA_A_A);
        vendor_A.getImages().add(imageB_A_A);
        vendor_A.getImages().add(imageA_B_A);

        Image imageA_A_B= new Image(vendor_B, postA_B,null);
        Image imageB_A_B= new Image(vendor_B, postA_B,null);
        Image imageA_B_B= new Image(vendor_B, postB_B,null);
        entityManager.persist(imageA_A_B);
        entityManager.persist(imageB_A_B);
        entityManager.persist(imageA_B_B);
        postA_B.getImages().add(imageA_A_B);
        postA_B.getImages().add(imageB_A_B);
        postB_B.getImages().add(imageA_B_B);
        vendor_B.getImages().add(imageA_A_B);
        vendor_B.getImages().add(imageB_A_B);
        vendor_B.getImages().add(imageA_B_B);


        List<Image> imageList= new ArrayList<>();
        imageRepository.findAll().forEach(imageList::add);

        List<Post> postList= new ArrayList<>();
        postRepository.findAll().forEach(postList::add);
        assertThat(postList.size(), is(equalTo(6)));

        Image someImage = imageList.get(1);
        System.out.println(someImage.getPost().getPostId());

        System.out.println(postA_A.getPostDate());
        assertThat(postA_A.getImages().size(),is(equalTo(2)));

        entityManager.remove(imageA_A_B);
        assertThat(postA_B.getImages().remove(imageA_A_B),is(equalTo(true)));
        assertThat(postA_B.getImages().size(),is(equalTo(1)));
    }

}
