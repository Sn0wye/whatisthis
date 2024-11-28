namespace Loan.Repository;

using Domain.Entities;

public interface ILoanRepository
{
    Task<LoanApplication?> FindAsync(int id);
    Task<LoanApplication> AddAsync(LoanApplication loanApplication);
}