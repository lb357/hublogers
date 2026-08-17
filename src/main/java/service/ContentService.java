package service;

import model.common.TransactionResult;
import model.composite.MetaHub;
import model.composite.MetaPost;
import model.data.Hub;
import model.data.Post;
import model.data.User;
import model.data.Vote;
import repository.composite.MetaHubRepository;
import repository.composite.MetaPostRepository;
import repository.data.HubRepository;
import repository.data.PostRepository;
import repository.data.UserRepository;
import repository.data.VoteRepository;
import util.AdminOutput;

import java.sql.SQLException;
import java.util.ArrayList;


public class ContentService {
    public static TransactionResult<Hub> createHub(String authToken, String hubname, String description) {
        if (hubname == null || description == null || hubname.isBlank() || description.isBlank()) {
            return TransactionResult.failResponse("Отсутствуют необходимые поля");
        }
        try {
            TransactionResult<User> userTransactionResult = AuthentificationService.authUser(authToken);
            if (userTransactionResult.isSuccess()) {
                User user = userTransactionResult.getData();
                Hub hub = HubRepository.createHub(
                        user.getId(),
                        hubname,
                        description
                );

                if (hub != null) {
                    return TransactionResult.successResponse(hub);
                } else {
                    return TransactionResult.failResponse("Хаб с таким названием уже существует");
                }
            } else {
                return userTransactionResult.transferFailure();
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Post> createPost(String authToken, Integer hubId, String label, String content) {
        if (label == null || content == null || label.isBlank() || content.isBlank()) {
            return TransactionResult.failResponse("Отсутствуют необходимые поля");
        }
        try {
            TransactionResult<User> userTransactionResult = AuthentificationService.authUser(authToken);
            if (userTransactionResult.isSuccess()) {
                User user = userTransactionResult.getData();
                Post post = PostRepository.createPost(
                        user.getId(),
                        hubId,
                        label,
                        content
                );
                if (post != null) {
                    return TransactionResult.successResponse(post);
                } else {
                    return TransactionResult.failResponse("Пост с таким заголовком уже существует");
                }
            } else {
                return userTransactionResult.transferFailure();
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Post> updatePost(String authToken, int postId, String content) {
        if (content == null || content.isBlank()){
            return TransactionResult.failResponse("Отсутствует контент поста");
        }

        try {
            TransactionResult<User> userTransactionResult = AuthentificationService.authUser(authToken);
            TransactionResult<MetaPost> postTransactionResult = getPost(postId);
            if (!userTransactionResult.isSuccess()) {return userTransactionResult.transferFailure();}
            else if (!postTransactionResult.isSuccess()) {return postTransactionResult.transferFailure();}
            else {
                User user = userTransactionResult.getData();
                if (user.getId().equals(postTransactionResult.getData().getPost().getAuthorId())) {
                    Post updated = PostRepository.updatePost(postId, content);
                    if (updated != null) {
                        return TransactionResult.successResponse(updated);
                    } else {
                        return TransactionResult.failResponse("Пост не был обновлен");
                    }
                } else {
                    return TransactionResult.failResponse("Нет прав на изменение поста");
                }
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<User> updateUser(String authToken, String status) {
        if (status == null || status.isBlank()){
            return TransactionResult.failResponse("Отсутствует статус пользователя");
        }

        try {
            TransactionResult<User> userTransactionResult = AuthentificationService.authUser(authToken);
            if (userTransactionResult.isSuccess()) {
                return TransactionResult.successResponse(UserRepository.updateUser(userTransactionResult.getData().getId(), status));
            } else {
                return userTransactionResult.transferFailure();
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }


    private static TransactionResult<Vote> votePost(String authToken, int postId, boolean vote) {
        try {
            TransactionResult<MetaPost> postTransactionResult = getPost(postId);
            if (!postTransactionResult.isSuccess()) {return postTransactionResult.transferFailure();}

            TransactionResult<User> userTransactionResult = AuthentificationService.authUser(authToken);
            if (!userTransactionResult.isSuccess()) {return userTransactionResult.transferFailure();}


            User user = userTransactionResult.getData();
            Post post = postTransactionResult.getData().getPost();
            if (post.getAuthorId() == user.getId()) {
                return TransactionResult.failResponse("Нельзя оставлять голос за свой пост");
            }

            return TransactionResult.successResponse(VoteRepository.votePostByUser(postId, user.getId(), vote));

        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Vote> likePost(String authToken, int postId) {
        return votePost(authToken, postId, true);
    }

    public static TransactionResult<Vote> dislikePost(String authToken, int postId) {
        return votePost(authToken, postId, false);
    }

    public static TransactionResult<Vote> getVote(String authToken, int postId) {
        try {
            TransactionResult<User> userTransactionResult = AuthentificationService.authUser(authToken);
            if (userTransactionResult.isSuccess()) {
                User user = userTransactionResult.getData();
                Vote vote = VoteRepository.getVote(postId, user.getId());
                return TransactionResult.successResponse(vote);
            } else {
                return userTransactionResult.transferFailure();
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<ArrayList<MetaPost>> getLastPosts(int page) {
        if (page < 0) {
            return TransactionResult.failResponse("Недопустимый номер страницы");
        }
        try {
            return TransactionResult.successResponse(MetaPostRepository.getLastMetaPosts(page));
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<ArrayList<MetaPost>> getTopPosts(int page) {
        if (page < 0) {
            return TransactionResult.failResponse("Недопустимый номер страницы");
        }
        try {
            return TransactionResult.successResponse(MetaPostRepository.getTopMetaPosts(page));
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<ArrayList<MetaPost>> findPosts(String query, int page) {
        if (page < 0) {
            return TransactionResult.failResponse("Недопустимый номер страницы");
        }
        if (query.length() < 3) {
            return TransactionResult.failResponse("Поисковый запрос должен содержать хотя бы 3 символа");
        }
        try {
            return TransactionResult.successResponse(MetaPostRepository.findMetaPosts(query, page));
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<ArrayList<MetaPost>> getHubPosts(int hubId, int page) {
        if (page < 0) {
            return TransactionResult.failResponse("Недопустимый номер страницы");
        }
        try {
            TransactionResult<MetaHub> hubTransactionResult = getHub(hubId);
            if (hubTransactionResult.isSuccess()) {
                return TransactionResult.successResponse(MetaPostRepository.getHubMetaPosts(hubId, page));
            } else {
                return hubTransactionResult.transferFailure();
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<ArrayList<MetaPost>> getUserPosts(int userId, int page) {
        if (page < 0) {
            return TransactionResult.failResponse("Недопустимый номер страницы");
        }
        try {
            TransactionResult<User> userTransactionResult = getUser(userId);
            if (userTransactionResult.isSuccess()) {
                return TransactionResult.successResponse(MetaPostRepository.getUserMetaPosts(userId, page));
            } else {
                return userTransactionResult.transferFailure();
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Integer> getPostPages(){
        try {
            return TransactionResult.successResponse(
                    (MetaPostRepository.metaPostCount()-1)/MetaPostRepository.getPageSize()
                    );
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Integer> findPostPages(String query){
        if (query.length() < 3) {
            return TransactionResult.failResponse("Поисковый запрос должен содержать хотя бы 3 символа");
        }
        try {
            return TransactionResult.successResponse(
                    (MetaPostRepository.findMetaPostCount(query)-1)/MetaPostRepository.getPageSize()
            );
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Integer> hubPostPages(int hubId){
        try {
            TransactionResult<MetaHub> hubTransactionResult = getHub(hubId);
            if (hubTransactionResult.isSuccess()) {
                return TransactionResult.successResponse(
                        (MetaPostRepository.hubMetaPostCount(hubId)-1)/MetaPostRepository.getPageSize()
                );
            } else {
                return hubTransactionResult.transferFailure();
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Integer> userPostPages(int userId){
        try {
            TransactionResult<User> userTransactionResult = getUser(userId);
            if (userTransactionResult.isSuccess()) {
                return TransactionResult.successResponse(
                        (MetaPostRepository.userMetaPostCount(userId)-1)/MetaPostRepository.getPageSize()
                );
            } else {
                return userTransactionResult.transferFailure();
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<MetaPost> getPost(int postId){
        try {
            MetaPost metaPost = MetaPostRepository.readMetaPost(postId);
            if (metaPost != null) {
                return TransactionResult.successResponse(metaPost);
            } else {
                return TransactionResult.failResponse("Пост не найден");
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<ArrayList<MetaHub>> getHubs(int page) {
        if (page < 0) {
            return TransactionResult.failResponse("Недопустимый номер страницы");
        }
        try {
            return TransactionResult.successResponse(MetaHubRepository.getMetaHubs(page));
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Integer> hubPages(){
        try {
            return TransactionResult.successResponse(
                    (MetaHubRepository.metaHubCount()-1)/MetaHubRepository.getPageSize()
            );
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<ArrayList<MetaHub>> getUserHubs(int creatorId, int page) {
        if (page < 0) {
            return TransactionResult.failResponse("Недопустимый номер страницы");
        }
        try {
            return TransactionResult.successResponse(MetaHubRepository.getUserMetaHubs(creatorId, page));
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Integer> userHubPages(int creatorId){
        try {
            return TransactionResult.successResponse(
                    (MetaHubRepository.userMetaHubCount(creatorId)-1)/MetaHubRepository.getPageSize()
            );
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<MetaHub> getHub(int hubId) {
        try {
            MetaHub hub = MetaHubRepository.readMetaHub(hubId);
            if (hub != null) {
                return TransactionResult.successResponse(hub);
            } else {
                return TransactionResult.failResponse("Хаб не найден");
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<User> getUser(int userId) {
        try {
            User user = UserRepository.readUser(userId);
            if (user != null) {
                return TransactionResult.successResponse(user);
            } else {
                return TransactionResult.failResponse("Пользователь не найден");
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }


}
