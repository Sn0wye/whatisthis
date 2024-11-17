using Loan.Domain.Enums;

namespace Loan.DTO.Response;

public class ApplyForLoanResponse
{
    public LoanApplicationStatus Status { get; set; }
    public required string Message { get; set; }
    public double Amount { get; set; }
    public int Term { get; set; }
    public Loan.Domain.Entities.LoanApplication? SuggestedLoan { get; set; }
}