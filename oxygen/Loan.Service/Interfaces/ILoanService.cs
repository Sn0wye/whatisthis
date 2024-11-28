using Loan.DTO;

namespace Loan.Service;

public interface ILoanService
{
    Task<LoanApplicationDTO> ApplyForLoan(string userId, double loanAmount, int term);
}