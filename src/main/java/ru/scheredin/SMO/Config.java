package ru.scheredin.SMO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ru.scheredin.SMO.api.AutoModeController;
import ru.scheredin.SMO.api.MainController;
import ru.scheredin.SMO.api.StepModeController;
import ru.scheredin.SMO.dto.Utils;
import ru.scheredin.SMO.services.AutoModeStatsService;
import ru.scheredin.SMO.services.ClockService;
import ru.scheredin.SMO.services.EvaluatorService;
import ru.scheredin.SMO.services.OrchestratorService;
import ru.scheredin.SMO.services.SnapshotService;

public class Config extends AbstractModule {
    @Override
    protected void configure() {
        bind(ClockService.class).toInstance(new ClockService());
        bind(AutoModeStatsService.class).toInstance(new AutoModeStatsService());
        bind(SnapshotService.class).toInstance(new SnapshotService());
        bind(OrchestratorService.class).toInstance(new OrchestratorService());
        bind(MainController.class).toInstance(new MainController());
        bind(StepModeController.class).toInstance(new StepModeController());
        bind(AutoModeController.class).toInstance(new AutoModeController());
        bind(ObjectMapper.class).toInstance(new ObjectMapper());
        bind(EvaluatorService.class).toInstance(new EvaluatorService());
        bind(Integer.class).annotatedWith(Names.named("MATH_ACCURACY")).toInstance(3);
        requestStaticInjection(Utils.class);
    }
}
