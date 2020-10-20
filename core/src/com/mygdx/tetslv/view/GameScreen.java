package com.mygdx.tetslv.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.tetslv.TetriSolver;
import com.mygdx.tetslv.model.GameField;
import com.mygdx.tetslv.model.TetPiece;

import static com.mygdx.tetslv.model.GameField.CELL_SIZE;
import static com.mygdx.tetslv.model.GameField.NUM_COLUMNS;
import static com.mygdx.tetslv.model.GameField.NUM_ROWS;

public class GameScreen extends AbstractScreen {

    private static final int STAGE_START_X = 25;
    private static final int STAGE_START_Y = 90;
    private static final int NEXT_TETPIECE_SIZE = 150;
    private static final int MIN_HORIZONTAL_MOVE_INTERVAL = 50;
    private static final int MIN_FALL_INTERVAL = 50;
    private static final int MIN_ROTATE_INTERVAL = 150;

    private Stage stage;
    private Image image;
    private long lastRotateMillis;
    private long lastHorizontalMoveMillis;
    private long lastFallMillis;
    private int score;
    private TetPiece currentTetpiece;
    private TetPiece nextTetpiece;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private GameField gameField;
    private float fallingSpeed;
    private boolean solverStatus;
    private int nextTetpieceBoxX = CELL_SIZE * NUM_COLUMNS + 2 * STAGE_START_X;
    private int nextTetpieceBoxY = STAGE_START_Y + CELL_SIZE * NUM_ROWS -  NEXT_TETPIECE_SIZE;
    private Label linesScore;
    private int savedScoreForSolver;
    private boolean paused = false;


    GameScreen(TetriSolver solver, int score, boolean solverStatus) {
        super(solver);
        this.score = score;
        if (solverStatus) this.savedScoreForSolver = score;
        stage = new Stage(new FitViewport(640, 800));
        this.solverStatus = solverStatus;
        Gdx.input.setInputProcessor(stage);
    }


    private boolean isKeyPressed(int hardKey) {
        return Gdx.input.isKeyPressed(hardKey);
    }


    @Override
    public void show() {
        Texture txt = new Texture(Gdx.files.internal("grey.jpg"));
        image = new Image(txt);
        stage.addActor(image);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, stage.getWidth(), stage.getHeight());
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        gameField = new GameField(solverStatus);
        fallingSpeed = 3.5f;
        currentTetpiece = TetPiece.newPiece(solverStatus);
        nextTetpiece = TetPiece.newPiece(solverStatus);

        gameField.setPosition(STAGE_START_X, STAGE_START_Y);
        stage.addActor(gameField);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        Skin skin = new Skin(Gdx.files.internal("shade_skin\\uiskin.json"));
        linesScore = new Label("Lines: " + score, skin.get("title-plain", Label.LabelStyle.class));

        TextButton lines = new TextButton(null, skin);
        lines.setLabel(linesScore);
        lines.setColor(Color.VIOLET);
        lines.getLabel().setAlignment(Align.center);
        lines.setTouchable(Touchable.disabled);

        TextButton pause = new TextButton(null, skin);
        pause.setLabel(new Label("Pause", skin.get("title-plain", Label.LabelStyle.class)));
        pause.getLabel().setAlignment(Align.center);
        pause.setColor(Color.VIOLET);

        TextButton reset = new TextButton(null, skin);
        reset.setLabel(new Label("Reset", skin.get("title-plain", Label.LabelStyle.class)));
        reset.getLabel().setAlignment(Align.center);
        reset.setColor(Color.VIOLET);

        TextButton exit = new TextButton(null, skin);
        exit.setLabel(new Label("Exit", skin.get("title-plain", Label.LabelStyle.class)));
        exit.getLabel().setAlignment(Align.center);
        exit.setColor(Color.VIOLET);


        pause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                paused = !paused;
            }
        });

        reset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameField.reset();
                if (solverStatus) score = savedScoreForSolver;
                else score = 0;
                currentTetpiece = TetPiece.newPiece(solverStatus);
                nextTetpiece = TetPiece.newPiece(solverStatus);
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                solver.setScreen(new MenuScreen(solver));
            }
        });

        table.add(lines).width( NEXT_TETPIECE_SIZE + 2).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(pause).width( NEXT_TETPIECE_SIZE + 2).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.row();
        table.add(reset).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(exit).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.setPosition(nextTetpieceBoxX  - 245 , nextTetpieceBoxY - 720 );

    }

    public static void renderPiece(ShapeRenderer renderer, int column, int row) {
        renderer.rect(STAGE_START_X + column * CELL_SIZE, STAGE_START_Y + row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void renderStage() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.BLACK);
        renderer.rect(nextTetpieceBoxX - 1, nextTetpieceBoxY - 1,  NEXT_TETPIECE_SIZE + 2,  NEXT_TETPIECE_SIZE + 2);
        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        currentTetpiece.render(renderer);
        nextTetpiece.render(renderer, nextTetpieceBoxX, nextTetpieceBoxY,  NEXT_TETPIECE_SIZE / 4);
        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.BLACK);
        currentTetpiece.render(renderer);
        nextTetpiece.render(renderer, nextTetpieceBoxX, nextTetpieceBoxY,  NEXT_TETPIECE_SIZE / 4);
        renderer.end();

    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        image.setSize(stage.getWidth(), stage.getHeight());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.6f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!paused) {
            if (TimeUtils.millis() - lastFallMillis > (1 / fallingSpeed) * 1000) {
                lastFallMillis = TimeUtils.millis();
                currentTetpiece.fall();
            } else if (isKeyPressed(Input.Keys.UP) && TimeUtils.millis() - lastRotateMillis > MIN_ROTATE_INTERVAL) {
                currentTetpiece.rotate(gameField);
                lastRotateMillis = TimeUtils.millis();
            }
            if (gameField.isOnGround(currentTetpiece.getCells())) {
                int numDeletedRows = gameField.placePiece(currentTetpiece.getCells());
                if (solverStatus) score -= numDeletedRows;
                else score += numDeletedRows;
                currentTetpiece = nextTetpiece;
                currentTetpiece.initialPosition();
                nextTetpiece = TetPiece.newPiece(solverStatus);
                if (gameField.isOnGround(currentTetpiece.getCells()) || (solverStatus && score <= 0)) {
                    solver.setScreen(new EndScreen(solver, score, solverStatus));
                }
            } else if (isKeyPressed(Input.Keys.LEFT) && TimeUtils.millis() - lastHorizontalMoveMillis > MIN_HORIZONTAL_MOVE_INTERVAL) {
                currentTetpiece.moveLeft(gameField);
                lastHorizontalMoveMillis = TimeUtils.millis();
            } else if (isKeyPressed(Input.Keys.RIGHT) && TimeUtils.millis() - lastHorizontalMoveMillis > MIN_HORIZONTAL_MOVE_INTERVAL) {
                currentTetpiece.moveRight(gameField);
                lastHorizontalMoveMillis = TimeUtils.millis();
            } else if (isKeyPressed(Input.Keys.DOWN) && TimeUtils.millis() - lastFallMillis > MIN_FALL_INTERVAL) {
                lastFallMillis = TimeUtils.millis();
                currentTetpiece.fall();
            }
        }

        camera.update();
        renderStage();
        linesScore.setText("Lines: " + score);
    }



    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        renderer.dispose();
    }
}
