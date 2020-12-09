package com.mygdx.tetslv.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.tetslv.TetrisSolver;
import com.mygdx.tetslv.model.GameField;
import com.mygdx.tetslv.model.Piece;
import com.mygdx.tetslv.model.Strategist;


import java.util.ArrayList;

import static com.mygdx.tetslv.model.GameField.CELL_SIZE;
import static com.mygdx.tetslv.model.GameField.NUM_COLUMNS;
import static com.mygdx.tetslv.model.GameField.NUM_ROWS;

public class GameScreen extends AbstractScreen {

    public static final int STAGE_START_X = 25;
    public static final int STAGE_START_Y = 90;
    private static final int NEXT_PIECE_SIZE = 150;
    private static final int MOVE = CELL_SIZE;
    public static final int NEXT_PIECE_BOXX = CELL_SIZE * NUM_COLUMNS + 2 * STAGE_START_X;
    public static final int NEXT_PIECE_BOXY = STAGE_START_Y + CELL_SIZE * NUM_ROWS -  NEXT_PIECE_SIZE;

    private Stage stage;
    private Image image;
    private Piece currentPiece;
    private Piece nextPiece;
    private AI ai;
    private OrthographicCamera camera;
    private ShapeRenderer renderer;
    private GameField gameField;
    private boolean solverStatus;
    private int nextPieceBoxX = CELL_SIZE * NUM_COLUMNS + 2 * STAGE_START_X;
    private int nextPieceBoxY = STAGE_START_Y + CELL_SIZE * NUM_ROWS -  NEXT_PIECE_SIZE;
    private Label linesScore;
    private Color color;
    private int savedScoreForSolver;
    private boolean paused = false;
    private int fallingSpeed = 32;

    GameScreen(TetrisSolver solver, int score, boolean solverStatus) {
        super(solver);
        if (solverStatus) {
            ai = new AI();
            ai.setSpeed(fallingSpeed);
            this.savedScoreForSolver = score;
        }
        stage = new Stage(new FitViewport(1000, 1080));
        this.solverStatus = solverStatus;
        Gdx.input.setInputProcessor(stage);
    }

    private class AI {

        static final int FULL_ROTATION = 4;

        int speed;

        AI() {
            speed = 1;
        }

        Piece choose(GameField gameField, Piece piece) {
            ArrayList<Strategist> strategies = new ArrayList<>();
            Piece newPiece = new Piece(piece);
            for (int i = 0; i < NUM_COLUMNS; i++) {
                for (int j = 0; j < FULL_ROTATION; j++) {
                    GameField newGameField = new GameField(gameField);
                    Piece falling = new Piece(newPiece);
                    while (!newGameField.update(falling)) {
                        falling.fall();
                    }
                    strategies.add(new Strategist(gameField, newGameField, newPiece));
                    newPiece.rotate();
                }
                newPiece.moveRight();
            }
            Strategist best = strategies.get(0);
            for (int i = 1; i < strategies.size(); i++) {
                best = strategies.get(i).getBetter(best);
            }
            return best.getPiece();
        }

        void play() {
            if (gameField.gameIsNotOver()) {
                currentPiece.changeY(-speed);
                if (gameField.update(currentPiece)) {
                    currentPiece.set(choose(gameField, nextPiece));
                    nextPiece = new Piece(NUM_ROWS, 0);
                }
                gameField.clearLine(solverStatus);
            }
        }

        void setSpeed(int speed) {
            this.speed = speed;
        }
    }



    @Override
    public void show() {
        Texture txt = new Texture(Gdx.files.internal("grey.jpg"));
        image = new Image(txt);
        stage.addActor(image);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, stage.getWidth(), stage.getHeight());
        renderer = new ShapeRenderer();
        gameField = new GameField();
        if (solverStatus)  {
            gameField.setScore(savedScoreForSolver);
            currentPiece = ai.choose(gameField, new Piece(NUM_ROWS, 0));
            color = Color.SCARLET;
        }
        else {
            currentPiece = new Piece(NUM_ROWS, 0);
            color = Color.ROYAL;
        }
        nextPiece = new Piece(NUM_ROWS, 0);

        gameField.setPosition(STAGE_START_X, STAGE_START_Y);
        gameField.setPosition(STAGE_START_X, STAGE_START_Y);
        stage.addActor(gameField);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        Skin skin = new Skin(Gdx.files.internal("shade_skin\\uiskin.json"));
        linesScore = new Label("Lines: " + gameField.getScore(), skin.get("title-plain", Label.LabelStyle.class));

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
                if (solverStatus) {
                    gameField.setScore(savedScoreForSolver);
                    currentPiece = ai.choose(gameField, new Piece(NUM_ROWS, 0));
                } else currentPiece = new Piece(NUM_ROWS, 0);
                nextPiece = new Piece(NUM_ROWS, 0);
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                solver.setScreen(new MenuScreen(solver));
            }
        });

        table.add(lines).width( NEXT_PIECE_SIZE + 2).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(pause).width( NEXT_PIECE_SIZE + 2).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.row();
        table.add(reset).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(exit).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.setPosition(nextPieceBoxX  - 425 , nextPieceBoxY - 1125 );

    }


    private void renderStage() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.BLACK);
        renderer.rect(nextPieceBoxX - 1, nextPieceBoxY - 1,  NEXT_PIECE_SIZE + 2,  NEXT_PIECE_SIZE + 2);
        renderer.end();

        currentPiece.fieldStateRender(gameField, renderer, color);
        currentPiece.renderPiece(currentPiece, renderer, color, false);
        nextPiece.renderPiece(nextPiece, renderer, color, true);
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
            if (gameField.gameIsNotOver() && !(solverStatus && gameField.getScore() == 0)) {
            if (solverStatus) {
                ai.play();
            } else {
                if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                    currentPiece.rotate();
                }
                else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    if (currentPiece.canPlace(-1)) {
                        currentPiece.changeX(-MOVE);
                    }
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    if (currentPiece.canPlace(1)) {
                        currentPiece.changeX(MOVE);
                    }
                } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    fallingSpeed = 32;
                } else {
                    fallingSpeed = 2;
                }
                    currentPiece.changeY(-fallingSpeed);
                    if (gameField.update(currentPiece)) {
                        currentPiece = nextPiece;
                        nextPiece = new Piece(NUM_ROWS, 0);
                    }
                    gameField.clearLine(solverStatus);
                }
            } else solver.setScreen(new EndScreen(solver, gameField.getScore(), solverStatus));
        }

        camera.update();
        renderStage();
        linesScore.setText("Lines: " + gameField.getScore());
    }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        renderer.dispose();
    }
}
