using System.Security.Claims;
using Loan.Domain.Enums;
using Loan.DTO.Response;
using Loan.Service;
using Microsoft.AspNetCore.Mvc;

namespace Loan.API.Controllers;

[ApiController]
[Route("loan")]
public class LoanController(ILoanService loanService) : ControllerBase
{
    [HttpPost("/apply")]
    public async Task<ActionResult<ApplyForLoanResponse>> ApplyForLoan([FromBody] ApplyForLoanRequest request)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);
        
        var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
        
        if (userId is null) return Unauthorized();

        var application = await loanService.ApplyForLoan(
            userId,
            request.LoanAmount,
            request.Term
        );

        string? message;
        if (application.SuggestedLoan is null)
        {
            message = application.LoanApplication.Status == LoanApplicationStatus.APPROVED
                ? "Loan approved :)"
                : "Loan rejected, unfortunately we don't have a better option for you at the moment :(";

            return new ApplyForLoanResponse
            {
                Message = message,
                ApplicationStatus = application.LoanApplication.Status,
                Amount = application.LoanApplication.Amount,
                Term = application.LoanApplication.Term,
                SuggestedLoan = null
            };
        }

        message = application.LoanApplication.Status == LoanApplicationStatus.APPROVED
            ? "Loan approved, but we have a better option for you!"
            : "Loan rejected, but we have a better option for you!";

        return new ApplyForLoanResponse
        {
            Message = message,
            ApplicationStatus = application.LoanApplication.Status,
            Amount = application.LoanApplication.Amount,
            Term = application.LoanApplication.Term,
            SuggestedLoan = application.SuggestedLoan
        };
    }
}