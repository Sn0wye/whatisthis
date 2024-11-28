using System.Runtime.Serialization;

namespace Loan.Domain.Enums;

public enum LoanApplicationStatus
{
    [EnumMember(Value = "PENDING")] PENDING,
    [EnumMember(Value = "APPROVED")] APPROVED,
    [EnumMember(Value = "REJECTED")] REJECTED
}