using System.ComponentModel.DataAnnotations.Schema;
using Loan.Domain.Enums;

namespace Loan.Domain.Entities;

[Table("loan_applications")]
public class LoanApplication
{
    public int Id { get; set; }
    public string UserId { get; set; }
    public LoanApplicationStatus Status { get; set; } = LoanApplicationStatus.PENDING;
    public double Amount { get; set; }
    public int Term { get; set; }

    public void ChangeStatus(LoanApplicationStatus applicationStatus)
    {
        Status = applicationStatus;
    }
}