package service;

import model.TransactionResult;
import model.composite.MetaHub;
import model.composite.MetaPost;
import model.domain.Hub;
import model.domain.Post;
import model.domain.User;
import model.domain.Vote;
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
        if (authToken == null || hubname == null || description == null || hubname.isBlank() || description.isBlank()) {
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
        if (authToken == null || label == null || content == null || label.isBlank() || content.isBlank()) {
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

    public static TransactionResult<Post> updatePost(String authToken, Integer postId, String content) {
        if (authToken == null) return TransactionResult.failResponse("Отсутствует authToken");
        if (postId == null) return TransactionResult.failResponse("Отсутствует postId");
        if (content == null) return TransactionResult.failResponse("Отсутствует content");
        if (content.isBlank()){
            return TransactionResult.failResponse("Текст поста пустой");
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
        if (authToken == null) return TransactionResult.failResponse("Отсутствует authToken");
        if (status == null) return TransactionResult.failResponse("Отсутствует status");
        if (status.isBlank()){
            return TransactionResult.failResponse("Статус пользователя пустой");
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


    private static TransactionResult<Vote> votePost(String authToken, Integer postId, Boolean vote) {
        if (authToken == null) return TransactionResult.failResponse("Отсутствует authToken");
        if (postId == null) return TransactionResult.failResponse("Отсутствует postId");
        if (vote == null) return TransactionResult.failResponse("Отсутствует vote");
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

    public static TransactionResult<Vote> likePost(String authToken, Integer postId) {
        if (postId == null) return TransactionResult.failResponse("Отсутствует postId");
        return votePost(authToken, postId, true);
    }

    public static TransactionResult<Vote> dislikePost(String authToken, Integer postId) {
        if (postId == null) return TransactionResult.failResponse("Отсутствует postId");
        return votePost(authToken, postId, false);
    }

    public static TransactionResult<Vote> getVote(String authToken, Integer postId) {
        if (authToken == null) return TransactionResult.failResponse("Отсутствует authToken");
        if (postId == null) return TransactionResult.failResponse("Отсутствует postId");
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

    public static TransactionResult<ArrayList<MetaPost>> getLastPosts(Integer page) {
        if (page == null) return TransactionResult.failResponse("Отсутствует page");
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

    public static TransactionResult<ArrayList<MetaPost>> getTopPosts(Integer page) {
        if (page == null) return TransactionResult.failResponse("Отсутствует page");
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

    public static TransactionResult<ArrayList<MetaPost>> findPosts(String query, Integer page) {
        if (page == null) return TransactionResult.failResponse("Отсутствует page");
        if (query == null) return TransactionResult.failResponse("Отсутствует query");
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

    public static TransactionResult<ArrayList<MetaPost>> getHubPosts(Integer hubId, Integer page) {
        if (hubId == null) return TransactionResult.failResponse("Отсутствует hubId");
        if (page == null) return TransactionResult.failResponse("Отсутствует page");
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

    public static TransactionResult<ArrayList<MetaPost>> getUserPosts(Integer userId, Integer page) {
        if (userId == null) return TransactionResult.failResponse("Отсутствует userId");
        if (page == null) return TransactionResult.failResponse("Отсутствует page");
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
        if (query == null) return TransactionResult.failResponse("Отсутствует query");
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

    public static TransactionResult<Integer> hubPostPages(Integer hubId){
        if (hubId == null) return TransactionResult.failResponse("Отсутствует hubId");
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

    public static TransactionResult<Integer> userPostPages(Integer userId){
        if (userId == null) return TransactionResult.failResponse("Отсутствует userId");
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

    public static TransactionResult<MetaPost> getPost(Integer postId){
        if (postId == null) return TransactionResult.failResponse("Отсутствует postId");
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

    public static TransactionResult<ArrayList<MetaHub>> getHubs(Integer page) {
        if (page == null) return TransactionResult.failResponse("Отсутствует page");
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

    public static TransactionResult<ArrayList<MetaHub>> getUserHubs(Integer creatorId, Integer page) {
        if (creatorId == null) return TransactionResult.failResponse("Отсутствует creatorId");
        if (page == null) return TransactionResult.failResponse("Отсутствует page");
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

    public static TransactionResult<Integer> userHubPages(Integer creatorId){
        if (creatorId == null) return TransactionResult.failResponse("Отсутствует creatorId");
        try {
            return TransactionResult.successResponse(
                    (MetaHubRepository.userMetaHubCount(creatorId)-1)/MetaHubRepository.getPageSize()
            );
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<MetaHub> getHub(Integer hubId) {
        if (hubId == null) return TransactionResult.failResponse("Отсутствует hubId");
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

    public static TransactionResult<User> getUser(Integer userId) {
        if (userId == null) return TransactionResult.failResponse("Отсутствует userId");
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
