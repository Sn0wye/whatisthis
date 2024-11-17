using Grpc.Net.Client;
using Pb;

namespace Loan.Infrastructure.Adapters;

public class UsersGRPCAdapter: IUsersGRPCAdapter
{
    private readonly UserService.UserServiceClient _client;

    public UsersGRPCAdapter()
    {
        var channel = GrpcChannel.ForAddress("http://127.0.0.1:50050");
        _client = new UserService.UserServiceClient(channel);
    }
    
    public async Task<User> GetUserAsync(string userId)
    {
        var request = new GetUserRequest
        {
            Id = userId
        };
        var response =  await _client.GetUserAsync(request);
        return response.User;
    }
    
}

public interface IUsersGRPCAdapter
{
    Task<User> GetUserAsync(string userId);
}
