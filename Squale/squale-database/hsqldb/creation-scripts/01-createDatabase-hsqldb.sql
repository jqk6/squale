
    alter table Analysis_Task 
        drop constraint FK91CAC9089ACA29CA;

    alter table Analysis_Task 
        drop constraint FK91CAC908E93A2E5E;

    alter table ApplicationLastExport 
        drop constraint FK1E3E973AB660AF72;

    alter table AuditDisplayConfBO 
        drop constraint FKBF5515D8CAD1505B;

    alter table AuditDisplayConfBO 
        drop constraint FKBF5515D8DA15C497;

    alter table AuditDisplayConfBO 
        drop constraint FKBF5515D8DD868E9C;

    alter table AuditGridBO 
        drop constraint FK71AB79AECAD1505B;

    alter table AuditGridBO 
        drop constraint FK71AB79AEDA15C497;

    alter table AuditGridBO 
        drop constraint FK71AB79AE49C5C1F2;

    alter table Component 
        drop constraint FK24013CDDEF730ACB;

    alter table Component 
        drop constraint FK24013CDDCF3F6BD7;

    alter table Component 
        drop constraint FK24013CDDECE51913;

    alter table Component 
        drop constraint FK24013CDDB2C13533;

    alter table Component 
        drop constraint FK24013CDDBEC27BED;

    alter table Component 
        drop constraint FK24013CDD499FD217;

    alter table Component 
        drop constraint FK24013CDDE6E62BC9;

    alter table Components_Audits 
        drop constraint FK5D3B0141CAD1505B;

    alter table Components_Audits 
        drop constraint FK5D3B01419164C9D;

    alter table CriteriumPractice_Rule 
        drop constraint FK8ADDE8664749294;

    alter table CriteriumPractice_Rule 
        drop constraint FK8ADDE8615B7FE16;

    alter table Error 
        drop constraint FK401E1E8CAD1505B;

    alter table Error 
        drop constraint FK401E1E8DA15C497;

    alter table FactorCriterium_Rule 
        drop constraint FK897340301C18583E;

    alter table FactorCriterium_Rule 
        drop constraint FK8973403064749294;

    alter table FactorRef 
        drop constraint FK2854B0A4A4AEA807;

    alter table FactorRef 
        drop constraint FK2854B0A497209814;

    alter table Formula_Conditions 
        drop constraint FKB3141771EB17C0F9;

    alter table Formula_Measures 
        drop constraint FK7A19C1CEA4104C92;

    alter table GridFactor_Rule 
        drop constraint FKBC5A70C61C18583E;

    alter table GridFactor_Rule 
        drop constraint FKBC5A70C649C5C1F2;

    alter table HomepageComponent 
        drop constraint FK8D93B88F22FEA8A7;

    alter table Mark 
        drop constraint FK247AED4AA85BD7;

    alter table Mark 
        drop constraint FK247AED9164C9D;

    alter table Measure 
        drop constraint FK9B263D3ECAD1505B;

    alter table Measure 
        drop constraint FK9B263D3E6C1EEACE;

    alter table Measure 
        drop constraint FK9B263D3E9164C9D;

    alter table Metric 
        drop constraint FK892AE1D029D429E5;

    alter table Module 
        drop constraint FK89B0928C7729BC88;

    alter table PracticeResult_Repartition 
        drop constraint FK84B2F9904AA85BD7;

    alter table Profile_DisplayConfBO 
        drop constraint FKC122A97DEA5281F2;

    alter table Profile_DisplayConfBO 
        drop constraint FKC122A97DBEC27CBB;

    alter table Profile_Rights 
        drop constraint FK62F82D6D21CAA303;

    alter table Profile_Rights 
        drop constraint FK62F82D6DC21CFAE3;

    alter table Profiles_Grids 
        drop constraint FK9949B158212FFC33;

    alter table Profiles_Grids 
        drop constraint FK9949B158BEC27CBB;

    alter table ProjectParameter 
        drop constraint FK6D7538B020F36050;

    alter table ProjectParameter 
        drop constraint FK6D7538B0CF7E4672;

    alter table QualityResult 
        drop constraint FKE9E7A9DCCAD1505B;

    alter table QualityResult 
        drop constraint FKE9E7A9DCDA15C497;

    alter table QualityResult 
        drop constraint FKE9E7A9DC6FA7AE5E;

    alter table QualityResult_Comment 
        drop constraint FKD36C3ADCCCF6BB41;

    alter table QualityRule 
        drop constraint FK420ADC7BE9F00F7;

    alter table Rule 
        drop constraint FK270B1C6C1EEACE;

    alter table RuleCheckingTransgressionItem 
        drop constraint FKDF0D4973CA94EE6;

    alter table RuleCheckingTransgressionItem 
        drop constraint FKDF0D4973979AE76D;

    alter table RuleCheckingTransgressionItem 
        drop constraint FKDF0D49739164C9D;

    alter table RuleCheckingTransgressionItem 
        drop constraint FKDF0D49736B5A5124;

    alter table RuleSet 
        drop constraint FKBF8713A6DA15C497;

    alter table Segment 
        drop constraint FKD8DD37132D40D50F;

    alter table Segment_Module 
        drop constraint FK25FDCD381A146766;

    alter table Segment_Module 
        drop constraint FK25FDCD38D4A7AD7B;

    alter table Segment_Segmentation 
        drop constraint FK4C70016EBBF32679;

    alter table Segment_Segmentation 
        drop constraint FK4C70016E1A146766;

    alter table SharedRepoStats 
        drop constraint FK3C971D48BBF32679;

    alter table SqualeReference 
        drop constraint FK32FD7E08499FD217;

    alter table Stats_squale_dict 
        drop constraint FK9B3A9E52EF730ACB;

    alter table Stats_squale_dict_annexe 
        drop constraint FKBC8FB71EEF730ACB;

    alter table Tag 
        drop constraint FK1477AA86C98F7;

    alter table Tag_Component 
        drop constraint FKE093EE589164C9D;

    alter table Tag_Component 
        drop constraint FKE093EE58FD9106F6;

    alter table TaskParameter 
        drop constraint FK16AD33849ACA29CA;

    alter table TaskRef 
        drop constraint FK797F8AE76E45E0C;

    alter table Termination_Task 
        drop constraint FKC739A2209ACA29CA;

    alter table Termination_Task 
        drop constraint FKC739A220E93A2E5E;

    alter table UserAccess 
        drop constraint FKB60A252FB4DCCF05;

    alter table UserBO 
        drop constraint FK97901978C21CFAE3;

    alter table User_Rights 
        drop constraint FK21885CCB22FEA975;

    alter table User_Rights 
        drop constraint FK21885CCBC21CFAE3;

    alter table User_Rights 
        drop constraint FK21885CCBB4DCCF05;

    alter table Volumetry_Measures 
        drop constraint FK92AE693393A162FA;

    drop table Analysis_Task if exists;

    drop table ApplicationLastExport if exists;

    drop table AtomicRights if exists;

    drop table AuditBO if exists;

    drop table AuditDisplayConfBO if exists;

    drop table AuditFrequency if exists;

    drop table AuditGridBO if exists;

    drop table Component if exists;

    drop table Components_Audits if exists;

    drop table CriteriumPractice_Rule if exists;

    drop table Error if exists;

    drop table FactorCriterium_Rule if exists;

    drop table FactorRef if exists;

    drop table Formula if exists;

    drop table Formula_Conditions if exists;

    drop table Formula_Measures if exists;

    drop table GridFactor_Rule if exists;

    drop table HomepageComponent if exists;

    drop table Job if exists;

    drop table Mark if exists;

    drop table Measure if exists;

    drop table Message if exists;

    drop table Metric if exists;

    drop table Module if exists;

    drop table News if exists;

    drop table PracticeResult_Repartition if exists;

    drop table ProfileBO if exists;

    drop table Profile_DisplayConfBO if exists;

    drop table Profile_Rights if exists;

    drop table Profiles_Grids if exists;

    drop table ProjectParameter if exists;

    drop table QualityGrid if exists;

    drop table QualityResult if exists;

    drop table QualityResult_Comment if exists;

    drop table QualityRule if exists;

    drop table Rule if exists;

    drop table RuleCheckingTransgressionItem if exists;

    drop table RuleSet if exists;

    drop table Segment if exists;

    drop table SegmentCategory if exists;

    drop table Segment_Module if exists;

    drop table Segment_Segmentation if exists;

    drop table Segmentation if exists;

    drop table Serveur if exists;

    drop table SharedRepoStats if exists;

    drop table SqualeParams if exists;

    drop table SqualeReference if exists;

    drop table Stats_squale_dict if exists;

    drop table Stats_squale_dict_annexe if exists;

    drop table StopTimeBO if exists;

    drop table Tag if exists;

    drop table TagCategory if exists;

    drop table Tag_Component if exists;

    drop table Task if exists;

    drop table TaskParameter if exists;

    drop table TaskRef if exists;

    drop table Tasks_User if exists;

    drop table Termination_Task if exists;

    drop table UserAccess if exists;

    drop table UserBO if exists;

    drop table User_Rights if exists;

    drop table Volumetry_Measures if exists;

    drop table adminParams if exists;

    drop table displayConf if exists;

    create table Analysis_Task (
        TasksUserId bigint not null,
        TaskRefId bigint not null,
        AnalysisTaskIndex integer not null,
        primary key (TasksUserId, AnalysisTaskIndex)
    );

    create table ApplicationLastExport (
        LastExportId bigint generated by default as identity (start with 1),
        ComponentId bigint,
        LastExportDate timestamp,
        ToExport bit,
        primary key (LastExportId),
        unique (ComponentId)
    );

    create table AtomicRights (
        AtomicRightsId bigint generated by default as identity (start with 1),
        Name varchar(255),
        primary key (AtomicRightsId)
    );

    create table AuditBO (
        AuditId bigint generated by default as identity (start with 1),
        Name varchar(255),
        auditDate timestamp,
        auditType varchar(255),
        Status integer not null,
        Comments varchar(255),
        historicalDate timestamp,
        Duration varchar(10),
        END_DATE timestamp,
        MAX_FILE_SYSTEM_SIZE bigint,
        BEGINNING_DATE timestamp,
        squale_version varchar(100),
        primary key (AuditId)
    );

    create table AuditDisplayConfBO (
        AuditConfId bigint generated by default as identity (start with 1),
        ConfId bigint,
        ProjectId bigint,
        AuditId bigint,
        primary key (AuditConfId)
    );

    create table AuditFrequency (
        AuditFrequencyId bigint generated by default as identity (start with 1),
        Nb_days integer not null,
        Frequency integer not null,
        primary key (AuditFrequencyId)
    );

    create table AuditGridBO (
        AuditGridId bigint generated by default as identity (start with 1),
        QualityGridId bigint,
        ProjectId bigint,
        AuditId bigint,
        primary key (AuditGridId)
    );

    create table Component (
        ComponentId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        Excluded bit not null,
        Justification varchar(4000),
        Name varchar(1024) not null,
        Parent bigint,
        ProjectId bigint,
        AuditFrequency integer,
        ResultsStorageOptions integer,
        Status integer,
        PublicApplication bit,
        LastUpdate timestamp,
        EXTERNAL_DEV bit,
        IN_PRODUCTION bit,
        lastUser varchar(1024),
        Serveur bigint,
        QualityApproachOnStart bit,
        InInitialDev bit,
        GlobalCost integer,
        DevCost integer,
        LongFileName varchar(2048),
        ProfileBO bigint,
        ParametersSet bigint,
        QualityGrid bigint,
        SourceManager bigint,
        StartLine integer,
        primary key (ComponentId)
    );

    create table Components_Audits (
        ComponentId bigint not null,
        AuditId bigint not null,
        primary key (ComponentId, AuditId)
    );

    create table CriteriumPractice_Rule (
        CriteriumRuleId bigint not null,
        Weight float not null,
        PracticeRuleId bigint not null,
        primary key (CriteriumRuleId, PracticeRuleId)
    );

    create table Error (
        ErrorId bigint generated by default as identity (start with 1),
        InitialMessage varchar(2048),
        Message varchar(2048),
        CriticityLevel varchar(255),
        TaskName varchar(255),
        AuditId bigint not null,
        ProjectId bigint not null,
        primary key (ErrorId)
    );

    create table FactorCriterium_Rule (
        FactorRuleId bigint not null,
        Weight float not null,
        CriteriumRuleId bigint not null,
        primary key (FactorRuleId, CriteriumRuleId)
    );

    create table FactorRef (
        ReferencielId bigint not null,
        Factor_Value float,
        Rule bigint not null,
        primary key (ReferencielId, Rule)
    );

    create table Formula (
        FormulaId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        ComponentLevel varchar(255),
        TriggerCondition varchar(4000),
        Formula varchar(4000),
        primary key (FormulaId)
    );

    create table Formula_Conditions (
        FormulaId bigint not null,
        Value varchar(4000),
        Rank integer not null,
        primary key (FormulaId, Rank)
    );

    create table Formula_Measures (
        FormulaId bigint not null,
        Measure varchar(255)
    );

    create table GridFactor_Rule (
        QualityGridId bigint not null,
        FactorRuleId bigint not null,
        primary key (QualityGridId, FactorRuleId)
    );

    create table HomepageComponent (
        HomepageComponentId bigint generated by default as identity (start with 1),
        ComponentName varchar(255) not null,
        UserBO bigint not null,
        ComponentPosition integer not null,
        ComponentValue varchar(255),
        primary key (HomepageComponentId)
    );

    create table Job (
        JobId bigint generated by default as identity (start with 1),
        JobName varchar(100),
        JobStatus varchar(100),
        JobDate timestamp,
        primary key (JobId)
    );

    create table Mark (
        MarkId bigint generated by default as identity (start with 1),
        Value float not null,
        ComponentId bigint not null,
        PracticeResultId bigint not null,
        primary key (MarkId)
    );

    create table Measure (
        MeasureId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        TaskName varchar(255),
        AuditId bigint not null,
        ComponentId bigint not null,
        RuleSetId bigint,
        primary key (MeasureId)
    );

    create table Message (
        MessageKey varchar(255) not null,
        lang varchar(6) not null,
        Title varchar(4000),
        Text varchar(4000) not null,
        primary key (MessageKey, lang)
    );

    create table Metric (
        MetricId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        Name varchar(255),
        MeasureId bigint,
        Blob_val longvarbinary,
        Boolean_val bit,
        Number_val float,
        String_val varchar(4000),
        primary key (MetricId)
    );

    create table Module (
        ModuleId bigint generated by default as identity (start with 1),
        Message varchar(255),
        Name varchar(255),
        RuleId bigint not null,
        primary key (ModuleId)
    );

    create table News (
        Id bigint generated by default as identity (start with 1),
        NewsKey varchar(4000) not null,
        Beginning_Date timestamp not null,
        End_Date timestamp not null,
        primary key (Id)
    );

    create table PracticeResult_Repartition (
        PracticeResultId bigint not null,
        Repartition_value integer,
        Repartition integer not null,
        primary key (PracticeResultId, Repartition)
    );

    create table ProfileBO (
        ProfileId bigint generated by default as identity (start with 1),
        Name varchar(255),
        primary key (ProfileId)
    );

    create table Profile_DisplayConfBO (
        ConfId bigint generated by default as identity (start with 1),
        Profile_ConfId bigint,
        ProfileId bigint,
        primary key (ConfId)
    );

    create table Profile_Rights (
        ProfileId bigint not null,
        Rights_Value varchar(255),
        AtomicRightsId bigint not null,
        primary key (ProfileId, AtomicRightsId)
    );

    create table Profiles_Grids (
        ProfileId bigint not null,
        GridId bigint not null,
        primary key (ProfileId, GridId)
    );

    create table ProjectParameter (
        ParameterId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        Value varchar(1024),
        ListId bigint,
        Rank integer,
        MapId bigint,
        IndexKey varchar(255),
        primary key (ParameterId)
    );

    create table QualityGrid (
        QualityGridId bigint generated by default as identity (start with 1),
        Name varchar(255) not null,
        DateOfUpdate timestamp not null,
        primary key (QualityGridId)
    );

    create table QualityResult (
        QualityResultId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        CreationDate timestamp,
        QualityRuleId bigint not null,
        MeanMark float not null,
        ProjectId bigint not null,
        AuditId bigint,
        primary key (QualityResultId)
    );

    create table QualityResult_Comment (
        QR_CommentId bigint generated by default as identity (start with 1),
        Comments varchar(4000),
        QualityResultId bigint,
        primary key (QR_CommentId),
        unique (QualityResultId)
    );

    create table QualityRule (
        QualityRuleId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        Help_Key varchar(255),
        Name varchar(255) not null,
        DateOfCreation timestamp not null,
        Formula bigint,
        WeightFunction varchar(255),
        effort integer,
        TimeLimitation varchar(6),
        Criticality integer,
        primary key (QualityRuleId)
    );

    create table Rule (
        RuleId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        Category varchar(255),
        Code varchar(255),
        RuleSetId bigint not null,
        Severity varchar(255),
        primary key (RuleId)
    );

    create table RuleCheckingTransgressionItem (
        ItemId bigint generated by default as identity (start with 1),
        Line integer not null,
        Path varchar(3000),
        Message varchar(3000) not null,
        ComponentId bigint,
        RuleId bigint not null,
        ComponentInvolvedId bigint,
        MeasureId bigint,
        primary key (ItemId)
    );

    create table RuleSet (
        RuleSetId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        Name varchar(255),
        DateOfUpdate timestamp not null,
        ProjectId bigint,
        FileContent longvarbinary,
        CppTestName varchar(255),
        Language varchar(255),
        primary key (RuleSetId),
        unique (CppTestName)
    );

    create table Segment (
        SegmentId bigint generated by default as identity (start with 1),
        Name varchar(255) not null,
        Identifier bigint not null,
        Deprecated bit not null,
        CategoryId bigint not null,
        primary key (SegmentId)
    );

    create table SegmentCategory (
        CategoryId bigint generated by default as identity (start with 1),
        Name varchar(255) not null,
        Identifier bigint not null,
        Type varchar(255) not null,
        Deprecated bit not null,
        primary key (CategoryId),
        unique (Identifier),
        unique (Name)
    );

    create table Segment_Module (
        SegmentId bigint not null,
        ComponentId bigint not null,
        primary key (SegmentId, ComponentId)
    );

    create table Segment_Segmentation (
        SegmentationId bigint not null,
        SegmentId bigint not null,
        primary key (SegmentationId, SegmentId)
    );

    create table Segmentation (
        SegmentationId bigint generated by default as identity (start with 1),
        primary key (SegmentationId)
    );

    create table Serveur (
        ServeurId bigint generated by default as identity (start with 1),
        Name varchar(255) not null,
        primary key (ServeurId),
        unique (Name)
    );

    create table SharedRepoStats (
        StatsId bigint generated by default as identity (start with 1),
        ElementType varchar(255),
        DataType varchar(255),
        DataName varchar(255),
        Language varchar(255),
        Mean float,
        MaxValue float,
        MinValue float,
        Deviation float,
        Elements integer,
        SegmentationId bigint not null,
        primary key (StatsId)
    );

    create table SqualeParams (
        SqualeParamsId bigint generated by default as identity (start with 1),
        ParamKey varchar(255) not null,
        ParamaValue varchar(255) not null,
        primary key (SqualeParamsId)
    );

    create table SqualeReference (
        ReferencielId bigint generated by default as identity (start with 1),
        QualityGrid bigint,
        PublicApplication bit,
        ApplicationName varchar(255),
        ProjectName varchar(255),
        ProjectLanguage varchar(255),
        ProgrammingLanguage varchar(255) not null,
        Version varchar(255),
        AuditDate timestamp,
        CodeLineNumber integer,
        MethodNumber integer,
        ClassNumber integer,
        HIDDEN bit not null,
        AUDIT_TYPE varchar(50) not null,
        primary key (ReferencielId)
    );

    create table Stats_squale_dict (
        Id bigint generated by default as identity (start with 1),
        NB_FACTEURS_ACCEPTES integer,
        NB_FACTEURS_ACCEPTES_RESERVES integer,
        NB_FACTEURS_REFUSES integer,
        NB_TOTAL_APPLI integer,
        NB_APPLI_AVEC_AUDIT_EXECUTE integer,
        NB_APPLI_AVEC_AUDIT_REUSSI integer,
        Date_calcul timestamp,
        ROI_EN_MHI double,
        NB_APPLI_A_VALIDER integer,
        NB_AUDITS_ECHECS integer,
        NB_AUDITS_PROGRAMME integer,
        NB_AUDITS_PARTIELS integer,
        NB_AUDITS_REUSSIS integer,
        NB_APPLI_AVEC_AUCUN_AUDIT integer,
        Serveur bigint,
        primary key (Id)
    );

    create table Stats_squale_dict_annexe (
        Id bigint generated by default as identity (start with 1),
        NB_LIGNES integer,
        Profil varchar(50),
        NB_PROJETS integer,
        Serveur bigint,
        primary key (Id)
    );

    create table StopTimeBO (
        StopTimeId bigint generated by default as identity (start with 1),
        DayOfWeek varchar(9) not null,
        TimeOfDay varchar(5) not null,
        primary key (StopTimeId)
    );

    create table Tag (
        TagId bigint generated by default as identity (start with 1),
        Name varchar(255) not null,
        Description varchar(1024) not null,
        TagCategory bigint,
        primary key (TagId),
        unique (Name)
    );

    create table TagCategory (
        TagCategoryId bigint generated by default as identity (start with 1),
        Name varchar(255) not null,
        Description varchar(1024) not null,
        primary key (TagCategoryId),
        unique (Name)
    );

    create table Tag_Component (
        ComponentId bigint not null,
        TagId bigint not null,
        primary key (ComponentId, TagId)
    );

    create table Task (
        TaskId bigint generated by default as identity (start with 1),
        Name varchar(255) not null,
        Class varchar(2048) not null,
        Configurable bit not null,
        Standard bit,
        Mandatory bit,
        primary key (TaskId),
        unique (Name)
    );

    create table TaskParameter (
        TaskParameterId bigint generated by default as identity (start with 1),
        Name varchar(255) not null,
        Value varchar(255) not null,
        TaskRefId bigint,
        primary key (TaskParameterId)
    );

    create table TaskRef (
        TaskRefId bigint generated by default as identity (start with 1),
        TaskId bigint,
        primary key (TaskRefId)
    );

    create table Tasks_User (
        AbstractTasksUserId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        Name varchar(255) not null,
        export_IDE bit,
        language varchar(255),
        MilestoneAudit bit,
        NormalAudit bit,
        primary key (AbstractTasksUserId),
        unique (Name)
    );

    create table Termination_Task (
        TasksUserId bigint not null,
        TaskRefId bigint not null,
        TerminationTaskIndex integer not null,
        primary key (TasksUserId, TerminationTaskIndex)
    );

    create table UserAccess (
        UserAccessId bigint generated by default as identity (start with 1),
        ApplicationId bigint,
        accessDate timestamp not null,
        matricule varchar(1024) not null,
        AccessIndex integer,
        primary key (UserAccessId)
    );

    create table UserBO (
        UserId bigint generated by default as identity (start with 1),
        FullName varchar(255),
        Matricule varchar(255),
        Password varchar(255),
        Email varchar(255),
        ProfileId bigint not null,
        unsubscribed bit,
        primary key (UserId)
    );

    create table User_Rights (
        UserId bigint not null,
        ProfileId bigint not null,
        ApplicationId bigint not null,
        primary key (UserId, ApplicationId)
    );

    create table Volumetry_Measures (
        VolumetryId bigint not null,
        Measure varchar(255) not null,
        primary key (VolumetryId, Measure)
    );

    create table adminParams (
        AdminParamsId bigint generated by default as identity (start with 1),
        paramKey varchar(255) not null,
        paramaValue varchar(255) not null,
        primary key (AdminParamsId)
    );

    create table displayConf (
        ConfId bigint generated by default as identity (start with 1),
        subclass varchar(255) not null,
        X_TRE varchar(400),
        Y_TRE varchar(400),
        X_POS bigint,
        Y_POS bigint,
        componentType varchar(255),
        primary key (ConfId)
    );

    alter table Analysis_Task 
        add constraint FK91CAC9089ACA29CA 
        foreign key (TaskRefId) 
        references TaskRef;

    alter table Analysis_Task 
        add constraint FK91CAC908E93A2E5E 
        foreign key (TasksUserId) 
        references Tasks_User;

    alter table ApplicationLastExport 
        add constraint FK1E3E973AB660AF72 
        foreign key (ComponentId) 
        references Component;

    alter table AuditDisplayConfBO 
        add constraint FKBF5515D8CAD1505B 
        foreign key (AuditId) 
        references AuditBO
        on delete cascade;

    alter table AuditDisplayConfBO 
        add constraint FKBF5515D8DA15C497 
        foreign key (ProjectId) 
        references Component
        on delete cascade;

    alter table AuditDisplayConfBO 
        add constraint FKBF5515D8DD868E9C 
        foreign key (ConfId) 
        references displayConf;

    alter table AuditGridBO 
        add constraint FK71AB79AECAD1505B 
        foreign key (AuditId) 
        references AuditBO
        on delete cascade;

    alter table AuditGridBO 
        add constraint FK71AB79AEDA15C497 
        foreign key (ProjectId) 
        references Component
        on delete cascade;

    alter table AuditGridBO 
        add constraint FK71AB79AE49C5C1F2 
        foreign key (QualityGridId) 
        references QualityGrid;

    alter table Component 
        add constraint FK24013CDDEF730ACB 
        foreign key (Serveur) 
        references Serveur;

    alter table Component 
        add constraint FK24013CDDCF3F6BD7 
        foreign key (SourceManager) 
        references Tasks_User;

    alter table Component 
        add constraint FK24013CDDECE51913 
        foreign key (ProjectId) 
        references Component
        on delete cascade;

    alter table Component 
        add constraint FK24013CDDB2C13533 
        foreign key (ParametersSet) 
        references ProjectParameter;

    alter table Component 
        add constraint FK24013CDDBEC27BED 
        foreign key (ProfileBO) 
        references Tasks_User;

    alter table Component 
        add constraint FK24013CDD499FD217 
        foreign key (QualityGrid) 
        references QualityGrid;

    alter table Component 
        add constraint FK24013CDDE6E62BC9 
        foreign key (Parent) 
        references Component
        on delete cascade;

    alter table Components_Audits 
        add constraint FK5D3B0141CAD1505B 
        foreign key (AuditId) 
        references AuditBO
        on delete cascade;

    alter table Components_Audits 
        add constraint FK5D3B01419164C9D 
        foreign key (ComponentId) 
        references Component
        on delete cascade;

    alter table CriteriumPractice_Rule 
        add constraint FK8ADDE8664749294 
        foreign key (CriteriumRuleId) 
        references QualityRule;

    alter table CriteriumPractice_Rule 
        add constraint FK8ADDE8615B7FE16 
        foreign key (PracticeRuleId) 
        references QualityRule;

    alter table Error 
        add constraint FK401E1E8CAD1505B 
        foreign key (AuditId) 
        references AuditBO
        on delete cascade;

    alter table Error 
        add constraint FK401E1E8DA15C497 
        foreign key (ProjectId) 
        references Component
        on delete cascade;

    alter table FactorCriterium_Rule 
        add constraint FK897340301C18583E 
        foreign key (FactorRuleId) 
        references QualityRule;

    alter table FactorCriterium_Rule 
        add constraint FK8973403064749294 
        foreign key (CriteriumRuleId) 
        references QualityRule;

    alter table FactorRef 
        add constraint FK2854B0A4A4AEA807 
        foreign key (ReferencielId) 
        references SqualeReference
        on delete cascade;

    alter table FactorRef 
        add constraint FK2854B0A497209814 
        foreign key (Rule) 
        references QualityRule
        on delete cascade;

    alter table Formula_Conditions 
        add constraint FKB3141771EB17C0F9 
        foreign key (FormulaId) 
        references Formula;

    alter table Formula_Measures 
        add constraint FK7A19C1CEA4104C92 
        foreign key (FormulaId) 
        references Formula;

    alter table GridFactor_Rule 
        add constraint FKBC5A70C61C18583E 
        foreign key (FactorRuleId) 
        references QualityRule;

    alter table GridFactor_Rule 
        add constraint FKBC5A70C649C5C1F2 
        foreign key (QualityGridId) 
        references QualityGrid;

    alter table HomepageComponent 
        add constraint FK8D93B88F22FEA8A7 
        foreign key (UserBO) 
        references UserBO;

    alter table Mark 
        add constraint FK247AED4AA85BD7 
        foreign key (PracticeResultId) 
        references QualityResult
        on delete cascade;

    alter table Mark 
        add constraint FK247AED9164C9D 
        foreign key (ComponentId) 
        references Component
        on delete cascade;

    alter table Measure 
        add constraint FK9B263D3ECAD1505B 
        foreign key (AuditId) 
        references AuditBO
        on delete cascade;

    alter table Measure 
        add constraint FK9B263D3E6C1EEACE 
        foreign key (RuleSetId) 
        references RuleSet;

    alter table Measure 
        add constraint FK9B263D3E9164C9D 
        foreign key (ComponentId) 
        references Component
        on delete cascade;

    alter table Metric 
        add constraint FK892AE1D029D429E5 
        foreign key (MeasureId) 
        references Measure
        on delete cascade;

    alter table Module 
        add constraint FK89B0928C7729BC88 
        foreign key (RuleId) 
        references Rule
        on delete cascade;

    alter table PracticeResult_Repartition 
        add constraint FK84B2F9904AA85BD7 
        foreign key (PracticeResultId) 
        references QualityResult
        on delete cascade;

    alter table Profile_DisplayConfBO 
        add constraint FKC122A97DEA5281F2 
        foreign key (Profile_ConfId) 
        references displayConf;

    alter table Profile_DisplayConfBO 
        add constraint FKC122A97DBEC27CBB 
        foreign key (ProfileId) 
        references Tasks_User;

    alter table Profile_Rights 
        add constraint FK62F82D6D21CAA303 
        foreign key (AtomicRightsId) 
        references AtomicRights;

    alter table Profile_Rights 
        add constraint FK62F82D6DC21CFAE3 
        foreign key (ProfileId) 
        references ProfileBO;

    alter table Profiles_Grids 
        add constraint FK9949B158212FFC33 
        foreign key (GridId) 
        references QualityGrid;

    alter table Profiles_Grids 
        add constraint FK9949B158BEC27CBB 
        foreign key (ProfileId) 
        references Tasks_User;

    alter table ProjectParameter 
        add constraint FK6D7538B020F36050 
        foreign key (ListId) 
        references ProjectParameter;

    alter table ProjectParameter 
        add constraint FK6D7538B0CF7E4672 
        foreign key (MapId) 
        references ProjectParameter;

    alter table QualityResult 
        add constraint FKE9E7A9DCCAD1505B 
        foreign key (AuditId) 
        references AuditBO
        on delete cascade;

    alter table QualityResult 
        add constraint FKE9E7A9DCDA15C497 
        foreign key (ProjectId) 
        references Component
        on delete cascade;

    alter table QualityResult 
        add constraint FKE9E7A9DC6FA7AE5E 
        foreign key (QualityRuleId) 
        references QualityRule;

    alter table QualityResult_Comment 
        add constraint FKD36C3ADCCCF6BB41 
        foreign key (QualityResultId) 
        references QualityResult
        on delete cascade;

    alter table QualityRule 
        add constraint FK420ADC7BE9F00F7 
        foreign key (Formula) 
        references Formula;

    alter table Rule 
        add constraint FK270B1C6C1EEACE 
        foreign key (RuleSetId) 
        references RuleSet
        on delete cascade;

    alter table RuleCheckingTransgressionItem 
        add constraint FKDF0D4973CA94EE6 
        foreign key (RuleId) 
        references Rule;

    alter table RuleCheckingTransgressionItem 
        add constraint FKDF0D4973979AE76D 
        foreign key (MeasureId) 
        references Measure
        on delete cascade;

    alter table RuleCheckingTransgressionItem 
        add constraint FKDF0D49739164C9D 
        foreign key (ComponentId) 
        references Component
        on delete cascade;

    alter table RuleCheckingTransgressionItem 
        add constraint FKDF0D49736B5A5124 
        foreign key (ComponentInvolvedId) 
        references Component;

    alter table RuleSet 
        add constraint FKBF8713A6DA15C497 
        foreign key (ProjectId) 
        references Component
        on delete cascade;

    alter table Segment 
        add constraint FKD8DD37132D40D50F 
        foreign key (CategoryId) 
        references SegmentCategory
        on delete cascade;

    alter table Segment_Module 
        add constraint FK25FDCD381A146766 
        foreign key (SegmentId) 
        references Segment
        on delete cascade;

    alter table Segment_Module 
        add constraint FK25FDCD38D4A7AD7B 
        foreign key (ComponentId) 
        references Component
        on delete cascade;

    alter table Segment_Segmentation 
        add constraint FK4C70016EBBF32679 
        foreign key (SegmentationId) 
        references Segmentation
        on delete cascade;

    alter table Segment_Segmentation 
        add constraint FK4C70016E1A146766 
        foreign key (SegmentId) 
        references Segment
        on delete cascade;

    alter table SharedRepoStats 
        add constraint FK3C971D48BBF32679 
        foreign key (SegmentationId) 
        references Segmentation
        on delete cascade;

    alter table SqualeReference 
        add constraint FK32FD7E08499FD217 
        foreign key (QualityGrid) 
        references QualityGrid
        on delete cascade;

    alter table Stats_squale_dict 
        add constraint FK9B3A9E52EF730ACB 
        foreign key (Serveur) 
        references Serveur;

    alter table Stats_squale_dict_annexe 
        add constraint FKBC8FB71EEF730ACB 
        foreign key (Serveur) 
        references Serveur;

    alter table Tag 
        add constraint FK1477AA86C98F7 
        foreign key (TagCategory) 
        references TagCategory;

    alter table Tag_Component 
        add constraint FKE093EE589164C9D 
        foreign key (ComponentId) 
        references Component
        on delete cascade;

    alter table Tag_Component 
        add constraint FKE093EE58FD9106F6 
        foreign key (TagId) 
        references Tag
        on delete cascade;

    alter table TaskParameter 
        add constraint FK16AD33849ACA29CA 
        foreign key (TaskRefId) 
        references TaskRef
        on delete cascade;

    alter table TaskRef 
        add constraint FK797F8AE76E45E0C 
        foreign key (TaskId) 
        references Task
		on delete cascade;

    alter table Termination_Task 
        add constraint FKC739A2209ACA29CA 
        foreign key (TaskRefId) 
        references TaskRef;

    alter table Termination_Task 
        add constraint FKC739A220E93A2E5E 
        foreign key (TasksUserId) 
        references Tasks_User;

    alter table UserAccess 
        add constraint FKB60A252FB4DCCF05 
        foreign key (ApplicationId) 
        references Component
		on delete cascade;

    alter table UserBO 
        add constraint FK97901978C21CFAE3 
        foreign key (ProfileId) 
        references ProfileBO;

    alter table User_Rights 
        add constraint FK21885CCB22FEA975 
        foreign key (UserId) 
        references UserBO;

    alter table User_Rights 
        add constraint FK21885CCBC21CFAE3 
        foreign key (ProfileId) 
        references ProfileBO;

    alter table User_Rights 
        add constraint FK21885CCBB4DCCF05 
        foreign key (ApplicationId) 
        references Component
        on delete cascade;

    alter table Volumetry_Measures 
        add constraint FK92AE693393A162FA 
        foreign key (VolumetryId) 
        references displayConf
        on delete cascade;
