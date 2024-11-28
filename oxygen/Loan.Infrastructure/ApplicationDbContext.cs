﻿using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;

namespace Loan.Infrastructure;

public class ApplicationDbContext : DbContext
{
    private readonly string _connectionString;
    
    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options, IConfiguration configuration)
        : base(options)
    {
        _connectionString = configuration.GetConnectionString("DefaultConnection");
    }
    
    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        optionsBuilder
            .UseNpgsql(_connectionString)
            .UseSnakeCaseNamingConvention();
    }
    
    public DbSet<Loan.Domain.Entities.LoanApplication> LoanApplications { get; set; }
}