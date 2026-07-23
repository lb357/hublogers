package service;

import model.common.TransactionResult;
import model.composite.MetaPost;
import model.data.Hub;
import model.data.Post;
import model.data.User;
import model.data.Vote;
import repository.composite.PostMetaRepository;
import repository.data.HubRepository;
import repository.data.PostRepository;
import repository.data.VoteRepository;

import java.util.ArrayList;

public class ContentService {
    public static TransactionResult<Hub> createHub(String authToken, String hubname, String description) {
        TransactionResult<User> userTransactionResult = AccountService.authUser(authToken);
        if (userTransactionResult.isSuccess()) {
            User user = userTransactionResult.getData();
            return HubRepository.createHub(
                    user.getId(),
                    hubname,
                    description
            );
        } else {
            return userTransactionResult.transferFail();
        }
    }

    public static TransactionResult<Post> createPost(String authToken, Integer hubId, String label, String content) {
        TransactionResult<User> userTransactionResult = AccountService.authUser(authToken);
        if (userTransactionResult.isSuccess()) {
            User user = userTransactionResult.getData();
            return PostRepository.createPost(user.getId(), hubId, label, content);
        } else {
            return userTransactionResult.transferFail();
        }
    }

    public static TransactionResult<Post> updatePost(String authToken, int postId, String content) {
        TransactionResult<User> userTransactionResult = AccountService.authUser(authToken);
        if (userTransactionResult.isSuccess()) {
            User user = userTransactionResult.getData();
            TransactionResult<Post> postTransactionResult = PostRepository.readPost(postId);
            if (postTransactionResult.isSuccess()) {
                Post post = postTransactionResult.getData();
                if (user.getId().equals(post.getAuthorId())) {
                    return PostRepository.updatePost(post.getId(), content);
                } else {
                    return new TransactionResult<>("Нет права на редактирование", 0);
                }
            } else {
                return postTransactionResult.transferFail();
            }
        } else {
            return userTransactionResult.transferFail();
        }
    }


    public static TransactionResult<Integer> deletePost(String authToken, int postId) {
        TransactionResult<User> userTransactionResult = AccountService.authUser(authToken);
        if (userTransactionResult.isSuccess()) {
            User user = userTransactionResult.getData();
            TransactionResult<Post> postTransactionResult = PostRepository.readPost(postId);
            if (postTransactionResult.isSuccess()) {
                Post post = postTransactionResult.getData();
                if (user.getId().equals(post.getAuthorId())) {
                    return PostRepository.deletePost(post.getId());
                } else {
                    return new TransactionResult<>("Нет права на удаление", 0);
                }
            } else {
                return postTransactionResult.transferFail();
            }
        } else {
            return userTransactionResult.transferFail();
        }
    }

    public static TransactionResult<Vote> like(String authToken, int postId) {
        TransactionResult<User> userTransactionResult = AccountService.authUser(authToken);
        if (userTransactionResult.isSuccess()) {
            User user = userTransactionResult.getData();
            return VoteRepository.votePostByUser(postId, user.getId(), true);
        } else {
            return userTransactionResult.transferFail();
        }
    }

    public static TransactionResult<Vote> dislike(String authToken, int postId) {
        TransactionResult<User> userTransactionResult = AccountService.authUser(authToken);
        if (userTransactionResult.isSuccess()) {
            User user = userTransactionResult.getData();
            return VoteRepository.votePostByUser(postId, user.getId(), false);
        } else {
            return userTransactionResult.transferFail();
        }
    }

    public static TransactionResult<Integer> unlike(String authToken, int postId) {
        TransactionResult<User> userTransactionResult = AccountService.authUser(authToken);
        if (userTransactionResult.isSuccess()) {
            User user = userTransactionResult.getData();
            return VoteRepository.unvotePostByUser(postId, user.getId());
        } else {
            return userTransactionResult.transferFail();
        }
    }

    public static TransactionResult<Vote> getVote(String authToken, int postId) {
        TransactionResult<User> userTransactionResult = AccountService.authUser(authToken);
        if (userTransactionResult.isSuccess()) {
            User user = userTransactionResult.getData();
            return VoteRepository.getVote(postId, user.getId());
        } else {
            return userTransactionResult.transferFail();
        }
    }


    public static TransactionResult<ArrayList<MetaPost>> getFeed(int page) {
        return PostMetaRepository.getMetaPosts(page);
    }

    public static TransactionResult<ArrayList<MetaPost>> findPosts(String query, int page) {
        return PostMetaRepository.findMetaPosts(query, page);
    }

    public static TransactionResult<ArrayList<MetaPost>> getHubPosts(int hubId, int page) {
        return PostMetaRepository.hubMetaPosts(hubId, page);
    }

    public static TransactionResult<Post> getPost(int postId){
        return PostRepository.readPost(postId);
    }

    public static TransactionResult<ArrayList<Hub>> getHubs() {
        return HubRepository.getHubs();
    }

    public static TransactionResult<Hub> getHub(int hubId) {
        return HubRepository.readHub(hubId);
    }

    public static TransactionResult<Integer> getLikes(int postId){
        return VoteRepository.getLikes(postId);
    }

    public static TransactionResult<Integer> getDislikes(int postId){
        return VoteRepository.getDislikes(postId);
    }
}
